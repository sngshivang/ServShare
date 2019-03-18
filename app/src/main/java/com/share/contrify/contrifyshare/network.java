package com.share.contrify.contrifyshare;

import android.content.Context;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;


public class network extends AsyncTask <Socket, Integer, Void> {

    @Override
    public Void doInBackground(Socket... sct)
    {
        class JavaHTTPServer implements Runnable{

            final File WEB_ROOT= Environment.getExternalStorageDirectory();
            String DEFAULT_FILE = "/relay";
            boolean rnr=true;
            private String bound="";
            final String FILE_NOT_FOUND = "404.html";
            ServerSocket serverConnect;
            final String METHOD_NOT_SUPPORTED = "not_supported.html";
            // port to listen connection
            final int PORT = 53000;

            // verbose mode
            final boolean verbose = true;
            // Client Connection via Socket Class
            private Socket connect;
            public JavaHTTPServer(Socket c) {

                connect = c;
            }
            public JavaHTTPServer()
            {

            }
            public void first() {
                try {
                    Log.i("FLOW","FISRT");
                    serverConnect = new ServerSocket(PORT);
                    System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");

                    // we listen until user halts server execution
                    while (rnr) {

                        Log.i("Waiting","1");
                        if (isCancelled())
                        {
                            serverConnect.close();
                            rnr=false;
                            Log.i("FLOW","ISCAN1");
                            forcestop();
                        }
                        else {
                            JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());
                            if (verbose) {
                                System.out.println("Connection opened. (" + new Date() + ")");
                            }

                            // create dedicated thread to manage the client connection
                            Thread thread = new Thread(myServer);
                            thread.start();
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Server Connection error : " + e.getMessage());
                }
            }

            @Override
            public void run() {
                // we manage our particular client connection
                BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
                BufferedInputStream bfis=null;
                FileOutputStream fos2=null;
                byte b[],bt2[],bt3[];
                BufferedWriter tout=null;
                String fileRequested = "";
                Log.i("FLOW","RUN");
                byte[] buffer = new byte[8192];
                FileInputStream fis=null;

                try {
                    if (isCancelled()) {
                        Log.i("FLOW", "ISCAN2");
                        rnr=false;
                        try {
                            out.close();
                            bfis.close();
                            dataOut.close();
                            connect.close();
                            serverConnect.close();// we close socket connection
                        } catch (Exception e) {
                            System.err.println("Error closing stream : " + e.getMessage());
                        }
                        finally {
                            forcestop();
                        }
                        //Thread.sleep(1000);
                    } else {
                        // we read characters from the client via input stream on the socket
                        bfis=new BufferedInputStream(connect.getInputStream());
                        b= new byte[1024];bt2= new byte[1024]; bt3 = new byte[1024];
                        //in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                        fos2 = new FileOutputStream(new File(WEB_ROOT,"google.txt"));
                        // we get character output stream to client (for headers)
                        out = new PrintWriter(connect.getOutputStream());
                        String method="";
                        // get binary output stream to client (for requested data)
                        dataOut = new BufferedOutputStream(connect.getOutputStream());
                        tout = new BufferedWriter(out);
                        int i=0,temp=0,j=3,lbc=0,ofc,off=0,b1=0,b2=0; boolean trip= true;
                        for (int readNum; (readNum = bfis.read(b)) != -1;)
                        {
                            int s=0;
                            if (i==0) {
                                while (b[s]!=32) {
                                    method+=(char)b[s];
                                    s++;
                                }
                                s++;
                                while (b[s]!=32) {
                                    fileRequested+=(char)b[s];
                                    s++;
                                }
                                System.out.println("Method: "+method);
                                System.out.println("File "+fileRequested);
                            }
                            if (method.equals("POST")||i!=0) {
                                off =readNum;
                                if (trip)
                                {
                                    bound=boundary(b);
                                }
                                for (j=0;j<1024&&trip;j++)
                                {
                                    if (lbc<18)
                                    {
                                        if (b[j]==13)
                                            lbc++;
                                    }
                                    else
                                    {
                                        temp=j+1;
                                        trip=false;
                                        for (int z=0;z<temp;z++)
                                            b[z]=0;
                                        break;
                                    }
                                }
                                if (!trip) {
                                    if(i==2){
                                        fos2.write(bt3,temp,Math.abs(1024-temp));
                                    }else if (i>2){
                                        fos2.write(bt3,0,1024);
                                    }
                                    fos2.flush();
                                    bt3 = bt2.clone();
                                    bt2 = b.clone();
                                    i++;

                                    if (readEnd(bt3,bt2,0)!=-1)
                                    {
                                        if (i<2)
                                        {
                                            byte[] nbt = new byte[1024];
                                            off = readEnd(b,nbt,temp);
                                            fos2.write(b,temp,Math.abs(off-2-temp));
                                        }
                                        else if (i==2)
                                        {
                                            off = readEnd(bt3,bt2,temp);
                                            if (off<=1024)
                                                fos2.write(bt3,temp,off-2);
                                            else
                                            {
                                                fos2.write(bt3, temp , 1024);
                                                fos2.write(bt2, 0, off-1024-2);
                                            }

                                        }
                                        else
                                        {
                                            off = readEnd(bt3,bt2,0);
                                            System.out.println("BEYOFF "+off);
                                            if (off<=1024)
                                                fos2.write(bt3,0,off-2);
                                            else
                                            {
                                                fos2.write(bt3, 0 , 1024);
                                                fos2.write(bt2, 0, (off-1024)-2);
                                            }

                                        }
                                        break;
                                    }

                                }
                            }
                            else break;
                        }
                        //String input = in.readLine();
                        System.out.println(method);
                        System.out.println(fileRequested);

                        // get first line of the request from the client
                        //String input = in.readLine();
                        //Log.i("input",input);
                        // we parse the request with a string tokenizer
                        //StringTokenizer parse = new StringTokenizer(input);
                        //String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                        // we get file requested
                        //fileRequested = parse.nextToken().toLowerCase();
                        Log.i("filereg", fileRequested);

                        // we support only GET and HEAD methods, we check
                        if (!method.equals("GET") && !method.equals("POST")) {
                            if (verbose) {
                                System.out.println("501 Not Implemented : " + method + " method.");
                            }

                            // we return the not supported file to the client
                            File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                            int fileLength = (int) file.length();
                            String contentMimeType = "text/html";
                            //read content to return to client
                            byte[] fileData = readFileData(file, fileLength);

                            // we send HTTP Headers with data to client
                            out.println("HTTP/1.1 501 Not Implemented");
                            out.println("Server: Java HTTP Server from SSaurel : 1.0");
                            out.println("Date: " + new Date());
                            out.println("Content-type: " + contentMimeType);
                            out.println("Content-length: " + fileLength);
                            out.println(); // blank line between headers and content, very important !
                            out.flush(); // flush character output stream buffer
                            // file
                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();

                        }
                        else if (method.equals("POST"))
                        {
                            Log.i("POST","Hey post request");
                            try {
                                DataInputStream dis = new DataInputStream(new BufferedInputStream(connect.getInputStream()));
                                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(connect.getOutputStream()));
//read the number of files from the client
                                int number = dis.readInt();
                                ArrayList<File> files = new ArrayList<>(number);
                                System.out.println("Number of Files to be received: " +number);
                                //read file names, add files to arraylist
                                for(i = 0; i< number;i++){
                                    File file = new File(dis.readUTF());
                                    files.add(file);
                                }
                                int n = 0;
                                byte[]buf = new byte[4092];

                                //outer loop, executes one for each file
                                for(i = 0; i < files.size();i++){

                                    System.out.println("Receiving file: " + files.get(i).getName());
                                    //create a new fileoutputstream for each new file
                                    FileOutputStream fos = new FileOutputStream(WEB_ROOT.getPath()+files.get(i).getName());
                                    //read file
                                    String line;
                                    while((line = in.readLine()) != null){
                                        fos.write(buf,0,n);
                                        fos.flush();
                                    }
                                    fos.close();
                                }

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                Log.e("POST",e.toString());

                            }
                        }
                        else
                         {
                            // GET or HEAD method
                            if (fileRequested.endsWith("/")) {
                                fileRequested += DEFAULT_FILE;
                                File file = new File(WEB_ROOT, DEFAULT_FILE);
                                int fileLength = (int) file.length();
                                String content = getContentType(fileRequested);

                                if (method.equals("GET")) { // GET method so we return content
                                    byte[] fileData = readFileData(file, fileLength);

                                    // send HTTP Headers
                                    out.println("HTTP/1.1 200 OK");
                                    out.println("Server: Java HTTP Server from SSaurel : 1.0");
                                    out.println("Date: " + new Date());
                                    out.println("Content-type: text/html");
                                    out.println("Content-length: " + fileLength);
                                    out.println(); // blank line between headers and content, very important !
                                    out.flush(); // flush character output stream buffer

                                    dataOut.write(fileData, 0, fileLength);
                                    dataOut.flush();
                                }

                                if (verbose) {
                                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                                }
                            } else if (isint(fileRequested)) {
                                Log.i("REQ", fileRequested);
                                File file = new File(WEB_ROOT, fileRequested);
                                int fileLength = (int) file.length();
                                if (method.equals("GET")) { // GET method so we return content
                                    byte[] fileData = readFileData(file, fileLength);
                                    out.println("HTTP/1.1 200 OK");
                                    out.println("Contrify Share Socket");
                                    out.println("Date: " + new Date());
                                    //out.println("Content-type: application/octet-stream");
                                    out.println("Content-length: " + fileLength);
                                    out.println(); // blank line between headers and content, very important !
                                    out.flush(); // flush character output stream buffer
                                    dataOut.write(fileData, 0, fileLength);
                                    dataOut.flush();
                                }
                            } else {
                                File file = new File(fileRequested);
                                Log.i("MiscFile", fileRequested);
                                int fileLength = (int) file.length();
                                fis = new FileInputStream(file);
                                int count;
                                if (method.equals("GET")) { // GET method so we return content
                                    //byte[] fileData = readFileData(file, fileLength);
                                    out.println("HTTP/1.1 200 OK");
                                    out.println("Contrify Share Socket");
                                    out.println("Date: " + new Date());
                                    //out.println("Content-type: application/octet-stream");
                                    out.println("Content-length: " + fileLength);
                                    out.println(); // blank line between headers and content, very important !
                                    out.flush(); // flush character output stream buffer
                                    while ((count = fis.read(buffer)) > 0) {
                                        dataOut.write(buffer, 0, count);
                                        dataOut.flush();
                                    }
                                    //dataOut.write(fileData, 0, fileLength);
                                    //dataOut.flush();
                                }
                            }

                        }
                    }

                    } catch(FileNotFoundException fnfe){
                        try {
                            fileNotFound(out, dataOut, fileRequested);
                        } catch (IOException ioe) {
                            System.err.println("Error with file not found exception : " + ioe.getMessage());
                        }

                    } catch(Exception ioe){
                        System.err.println("Server error : " + ioe);
                    } finally{
                        try {
                            bfis.close();
                            out.close();
                            dataOut.close();
                            connect.close(); // we close socket connection
                            getallthr();
                        } catch (Exception e) {
                            System.err.println("Error closing stream : " + e.getMessage());
                        }

                        if (verbose) {
                            System.out.println("Connection closed.\n");
                        }
                    }

            }
            private boolean isint(String inp)
            {
                if (inp.length()>20) {
                    if (inp.substring(1, 21).equals("bootstrap-4.0.0-dist") )
                        return true;
                    else
                        return false;
                }
                else
                    return false;
            }
            private void forcestop()
            {
                        System.out.println("Force stop called");
                        //Set<Thread> thr = Thread.getAllStackTraces().keySet();
                        //Iterator it = thr.iterator();
                        for (Thread t : Thread.getAllStackTraces().keySet())
                        {  if (t.getState()==Thread.State.RUNNABLE)
                            {
                            System.out.println("Interrupter");
                            t.interrupt();
                        }
                        }


            }
            private void getallthr()
            {
                for (Thread t : Thread.getAllStackTraces().keySet())
                {
                    Log.i("Thread",t.getName());
                }
            }
            private byte[] readFileData(File file, int fileLength) throws IOException {
                FileInputStream fileIn = null;
                byte[] fileData = new byte[fileLength];

                try {
                    fileIn = new FileInputStream(file);
                    fileIn.read(fileData);
                } finally {
                    if (fileIn != null)
                        fileIn.close();
                }

                return fileData;
            }

            // return supported MIME Types
            private String getContentType(String fileRequested) {
                if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
                    return "text/html";
                else
                    return "text/plain";
            }

            private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
                File file = new File(WEB_ROOT, FILE_NOT_FOUND);
                int fileLength = (int) file.length();
                String content = "text/html";
                byte[] fileData = readFileData(file, fileLength);

                out.println("HTTP/1.1 404 File Not Found");
                out.println("Server: Java HTTP Server from SSaurel : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + content);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer

                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

                if (verbose) {
                    System.out.println("File " + fileRequested + " not found");
                }
            }
            private int readEnd(byte[] ba, byte[] bb,  int j)
            {
                int i;
                for (i=0;i<j;i++)
                    ba[i]=0;
                byte[] bc = new byte[2048];
                for (i=0;i<1024;i++)
                    bc[i] = ba[i];
                for (i=1024;i<2048;i++)
                    bc[i] = bb[i-1024];
                int fnd = KMPSearch(bound,bc);
                return fnd;

            }
            private String cntlnt(byte[] b)
            {
                int fnd = KMPSearch("Content-Length:",b);
                String out = "";
                for (int i=fnd+16;b[i]!=13;i++)
                    out=out+b[i];
                return out;
            }
            int KMPSearch(String pat, byte[] txt)
            {
                int M = pat.length(),fnd=-1;
                int N = 2048;
                int lps[] = new int[M];
                int j = 0; // index for pat[]
                computeLPSArray(pat, M, lps);

                int i = 0; // index for txt[]
                while (i < N) {
                    if (pat.charAt(j) == (char)txt[i]) {
                        j++;
                        i++;
                    }
                    if (j == M) {
                        fnd = i-j;
                        j = lps[j - 1];
                        break;
                    }

                    // mismatch after j matches
                    else if (i < N && pat.charAt(j) != (char)txt[i]) {
                        if (j != 0)
                            j = lps[j - 1];
                        else
                            i = i + 1;
                    }
                }
                return fnd;
            }
            //private void finalise(int i,)
            void computeLPSArray(String pat, int M, int lps[])
            {
                // length of the previous longest prefix suffix
                int len = 0;
                int i = 1;
                lps[0] = 0; // lps[0] is always 0

                // the loop calculates lps[i] for i = 1 to M-1
                while (i < M) {
                    if (pat.charAt(i) == pat.charAt(len)) {
                        len++;
                        lps[i] = len;
                        i++;
                    }
                    else // (pat[i] != pat[len])
                    {
                        // This is tricky. Consider the example.
                        // AAACAAAA and i = 7. The idea is similar
                        // to search step.
                        if (len != 0) {
                            len = lps[len - 1];

                            // Also, note that we do not increment
                            // i here
                        }
                        else // if (len == 0)
                        {
                            lps[i] = len;
                            i++;
                        }
                    }
                }
            }
            private String boundary(byte[] b)
            {
                int j;
                String bstr="";
                for (j=0;j<1020;j++)
                {
                    if (b[j]=='-'&&b[j+1]=='-'&&b[j+2]=='-')
                    {
                        while (b[j]!=13)
                        {
                            bstr=bstr+(char)b[j];
                            j++;
                        }
                        break;

                    }
                }
                bstr = "--"+bstr;
                return bstr;
            }
        }
        boolean tsr=true;
        JavaHTTPServer js = new JavaHTTPServer();
        Log.i("FLOW","MAIN");
        /*while (tsr) {
            //js.first();
            if (isCancelled()) {
                Log.i("FLOW", "ISCAN3");
                js.forcestop();
                tsr = false;
            }
        }*/
        js.first();
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... val) {

    }
    protected void stopper()
    {

    }

}
