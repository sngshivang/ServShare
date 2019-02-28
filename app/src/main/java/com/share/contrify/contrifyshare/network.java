package com.share.contrify.contrifyshare;

import android.content.Context;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                BufferedWriter tout=null;
                String fileRequested = null;
                Log.i("FLOW","RUN");
                byte[] buffer = new byte[8192];
                FileInputStream fis=null;

                try {
                    if (isCancelled()) {
                        Log.i("FLOW", "ISCAN2");
                        rnr=false;
                        try {
                            in.close();
                            out.close();
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
                        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                        // we get character output stream to client (for headers)
                        out = new PrintWriter(connect.getOutputStream());
                        // get binary output stream to client (for requested data)
                        dataOut = new BufferedOutputStream(connect.getOutputStream());
                        tout = new BufferedWriter(out);

                        // get first line of the request from the client
                        String input = in.readLine();
                        //Log.i("input",input);
                        // we parse the request with a string tokenizer
                        StringTokenizer parse = new StringTokenizer(input);
                        String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                        // we get file requested
                        fileRequested = parse.nextToken().toLowerCase();
                        Log.i("filereg", fileRequested);

                        // we support only GET and HEAD methods, we check
                        if (!method.equals("GET") && !method.equals("HEAD")) {
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

                        } else {
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
                            in.close();
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
