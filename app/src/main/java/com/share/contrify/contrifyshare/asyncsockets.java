package com.share.contrify.contrifyshare;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

/*public class asyncsockets extends AsyncTask <String, Void, Integer> {

    private static String TAG = "Hi, Yo. You are communicating with the server.";
    Uri video = Uri.parse("android.resource://com.share.contrify.contrifyshare/raw/rewind_results");
    private String DEFAULT_FILE;
    private static final String FILE_NOT_FOUND = "404.html";
    private static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    private String fileRequested=null;
    private Socket socket;
    File WEB_ROOT;

    public Integer doInBackground(String... urls)
    {
        WEB_ROOT = Environment.getExternalStorageDirectory();
        //WEB_ROOT = new File("File:///sdcard");
        DEFAULT_FILE = "/rewind_results";
        //InputStream is = this.getResources().openRawResource(R.raw.rewind_results);
        //DEFAULT_FILE = is.toString();
        try{
            ServerSocket server = new ServerSocket(53000);
            Log.i("Info","Server has started");
            while (true){
                //socket=server.accept();
                MainActivity mt = new MainActivity(server.accept());
                Log.i("Accepted","Server has accepted a conenction");
                new Thread(mt).start();
            }}
        catch (Exception e)
        {
            Log.e("Error",e.toString());
        }
        return 0;
    }
    public void run() {
        try {
            while (true)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                String input = in.readLine();
                // we parse the request with a string tokenizer
                StringTokenizer parse = new StringTokenizer(input);
                String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                // we get file requested
                fileRequested = parse.nextToken().toLowerCase();

                // we support only GET and HEAD methods, we check
                if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                    if (true) {
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
                    }

                    File file = new File(WEB_ROOT, fileRequested);
                    int fileLength = (int) file.length();
                    String content = getContentType(fileRequested);

                    if (method.equals("GET")) { // GET method so we return content
                        byte[] fileData = readFileData(file, fileLength);

                        // send HTTP Headers
                        out.println("HTTP/1.1 200 OK");
                        out.println("Server: Java HTTP Server from SSaurel : 1.0");
                        out.println("Date: " + new Date());
                        out.println("Content-type: " + content);
                        out.println("Content-length: " + fileLength);
                        out.println(); // blank line between headers and content, very important !
                        out.flush(); // flush character output stream buffer

                        dataOut.write(fileData, 0, fileLength);
                        dataOut.flush();
                    }

                    if (true) {
                        System.out.println("File " + fileRequested + " of type " + content + " returned");
                    }

                }



                in.close();
                out.close();
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }
    }*/

