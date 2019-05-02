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
            String DEFAULT_FILE = "/servshare_main.html";
            boolean rnr=true;
            private String bound="", frn="SVSHARE_ERR_NF";;
            int cnt=0,tc=0;
            final String FILE_NOT_FOUND = "404.html";
            ServerSocket serverConnect;
            final String METHOD_NOT_SUPPORTED = "not_supported.html";
            // port to listen connection
            final int PORT = universals.port;

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
                    serverConnect = new ServerSocket(PORT);
                    System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
                    // we listen until user halts server execution
                    while (rnr) {
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
                BufferedOutputStream fos2=null;
                byte b[],bt2[],bt3[];
                BufferedWriter tout=null;
                String fileRequested = "";
                Log.i("FLOW","RUN");
                byte[] buffer = new byte[8192];
                FileInputStream fis=null;

                try {
                    if (isCancelled()) {
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
                        b= new byte[8192];bt2= new byte[8192]; bt3 = new byte[8192];
                        //in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                        // we get character output stream to client (for headers)
                        out = new PrintWriter(connect.getOutputStream());
                        String method="";
                        new File(WEB_ROOT,"servshare_files").mkdirs();
                        // get binary output stream to client (for requested data)
                        dataOut = new BufferedOutputStream(connect.getOutputStream());
                        int i=0,temp=0,j=3,lbc=0,stop=3,crp=0,mpos=0;
                        boolean trip= true,rdvl=true,wl=false,pwr=true,trip4=true,trip2=false;
                        while(true)
                        {
                            if (rdvl)
                            {
                                if (bfis.read(b)==-1)
                                    break;
                            }
                            if (stop==0)
                                break;
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
                            }
                            if (method.equals("POST")||i!=0) {
                                if (trip)
                                    bound=boundary(b);
                                for (j=0;j<8192&&trip;j++)
                                {
                                    if (lbc<13)
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
                                    if (wl)
                                    {
                                        crp-=8192;
                                        cnt=0;
                                        mpos = ffname(bt3,bt2,crp);
                                        if (!frn.equals("SVSHARE_ERR_NF")&&mpos!=-1)
                                        {
                                            trip2=true;
                                            wl=false;
                                        }
                                        else {
                                            wl=true;
                                        }
                                        //System.out.println("wl val "+frn+" "+mpos);
                                    }
                                    else if (!trip2&&(crp=readEnd(bt3,bt2,0,bound))!=-1&&tc<21)
                                    {
                                        System.out.println("Else if called");
                                        tc++;
                                        if (crp<8192)
                                        {
                                            s=crp+bound.length();
                                            for (int p=0;p<s&&p<8192;p++)
                                                bt3[p]=0;
                                        }
                                        else {
                                            s=crp-8192;
                                            s+=bound.length();
                                            for (int p=0;p<8192;p++)
                                                bt3[p]=0;
                                            for (int p=0;p<s&&p<8192;p++)
                                                bt2[p]=0;
                                        }

                                        mpos = ffname(bt3,bt2,crp);
                                        if (!frn.equals("SVSHARE_ERR_NF")&&mpos!=-1)
                                        {
                                            trip2=true;
                                            wl=false;
                                        }
                                        else {
                                            wl=true;
                                        }
                                    }
                                    if (readEnd(bt2,b,0,bound+"--")==-1)
                                        rdvl=true;
                                    else
                                        rdvl=false;
                                    pwr=true;
                                    if (trip2&&!wl) {
                                        if (trip4)
                                        {
                                            if (frn.equals(""))
                                                frn="BLANKFILE";
                                            try {
                                                fos2 = new BufferedOutputStream(new FileOutputStream(new File(WEB_ROOT+"/servshare_files/",frn)));
                                            }
                                            catch(Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                            trip4=false;
                                        }
                                        if ((crp=readEnd(bt3,bt2,0,bound))!=-1)
                                        {
                                            tc++;
                                            int ofst=0;
                                            if (mpos!=-1)
                                                ofst=mpos+2;
                                            //System.out.println(ofst+" offset");
                                            if (crp<1024)
                                            {
                                                fos2.write(bt3,ofst,(Math.abs(crp-ofst))-2);
                                                for (int z=0;z<crp;z++)
                                                    bt3[z]=0;
                                            }
                                            else
                                            {
                                                crp-=8192;
                                                if (ofst<8192) {
                                                    fos2.write(bt3,ofst,8192-ofst);
                                                    fos2.write(bt2,0,crp-2);
                                                }
                                                else
                                                {
                                                    ofst-=8192;
                                                    fos2.write(bt2, ofst, (Math.abs(ofst-crp))-2);
                                                }
                                                for (int z=0;z<8192;z++)
                                                    bt3[z]=0;
                                                for (int z=0;z<crp;z++)
                                                    bt2[z]=0;
                                            }
                                            fos2.close();
                                            if (readEnd(bt3,bt2,0,bound+"--")!=-1&&tc>19)
                                            {
                                                tc=0;
                                                break;
                                            }
                                            trip4=true;
                                            cnt=0;mpos=-1;
                                            pwr = false;
                                            rdvl=false;
                                            trip2=false;
                                            wl=false;
                                            frn="SVSHARE_ERR_NF";
                                        }

                                        if (mpos!=-1&&pwr)
                                        {
                                            if (mpos<8192) {
                                                fos2.write(bt3, mpos+2, (8192-mpos)-2);
                                                mpos=-1;
                                            }
                                            else
                                                mpos-=8192;
                                        }
                                        else if (trip2&&!wl&&pwr)
                                            fos2.write(bt3, 0, 8192);
                                        fos2.flush();
                                    }
                                    if (pwr) {
                                        bt3 = bt2.clone();
                                        bt2 = b.clone();
                                    }
                                }
                                i++;
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
                        else
                        {
                            // GET or HEAD method
                            if (fileRequested.endsWith("/")) {
                                fileRequested += DEFAULT_FILE;
                                File file = new File(WEB_ROOT, DEFAULT_FILE);
                                int fileLength = (int) file.length();
                                String content = getContentType(fileRequested);

                                if (method.equals("GET")||method.equals("POST")) { // GET method so we return content
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
                                if (method.equals("GET")||method.equals("POST")) { // GET method so we return content
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
                                fileRequested=fileRequested.replace("%20"," ");
                                File file = new File(fileRequested);
                                Log.i("MiscFile", fileRequested);
                                int fileLength = (int) file.length();
                                fis = new FileInputStream(file);
                                int count;
                                if (method.equals("GET")||method.equals("POST")) { // GET method so we return content
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
                if (inp.length()>15) {
                    if (inp.substring(1, 10).equals("servshare"))
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
                for (Thread t : Thread.getAllStackTraces().keySet())
                {  if (t.getState()==Thread.State.RUNNABLE)
                {
                    System.out.println("Interrupter");
                    t.interrupt();
                }
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
            private int readEnd(byte[] ba, byte[] bb,  int j, String inps)
            {
                int i;
                for (i=0;i<j;i++)
                    ba[i]=0;
                byte[] bc = new byte[16384];
                for (i=0;i<8192;i++)
                    bc[i] = ba[i];
                for (i=8192;i<16384;i++)
                    bc[i] = bb[i-8192];
                return (KMPSearch(inps,bc));

            }
            private int KMPSearch(String pat, byte[] txt)
            {
                int M = pat.length(),fnd=-1;
                int N = 16384;
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
                    else if (i < N && pat.charAt(j) != (char)txt[i]) {
                        if (j != 0)
                            j = lps[j - 1];
                        else
                            i = i + 1;
                    }
                }
                return fnd;
            }
            private void computeLPSArray(String pat, int M, int lps[])
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
                    else
                    {
                        if (len != 0) {
                            len = lps[len - 1];
                        }
                        else
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
                for (j=0;j<8188;j++)
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
            private int ffname(byte[] ba, byte[] bb,  int j)
            {
                int i,spos=-1; boolean trip=false,trip2=false;
                String fname="";
                byte[] bc = new byte[16384];
                for (i=0;i<8192;i++)
                    bc[i] = ba[i];
                for (i=8192;i<16384;i++)
                    bc[i] = bb[i-8192];
                int pos=KMPSearch("filename",bc);
                if (pos!=-1)
                {
                    pos+=10;
                    while (pos<16384&&bc[pos]!='"')
                    {
                        if (bc[pos]!='"') {
                            fname+=(char)bc[pos];
                            pos++;
                        }
                        else
                        {
                            trip=true;
                            break;
                        }
                    }

                    if (pos>16383&&!trip)
                    {
                        trip2=true;
                    }
                }

                else trip2=true;
                pos=j;
                while (pos<16384&&cnt<3)
                {
                    if (bc[pos]==13)
                        cnt++;
                    pos++;
                }
                if (!(pos>16383&&cnt<3))
                    spos=pos+1;
                if (trip2)
                    frn="SVSHARE_ERR_NF";
                else
                    frn=fname;
                return spos;

            }
        }
        JavaHTTPServer js = new JavaHTTPServer();
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