package com.share.contrify.contrifyshare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    int perval = 1;
    Uri currFileURI;
    String unifil=null;
    public String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        perval);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //DEFAULT_FILE = "android.resource://" + this.getPackageName() + "/raw/rewind_results"; //+ R.raw.rewind_results;
        //DEFAULT_FILE=Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rewind_results).toString();
        //DEFAULT_FILE="file:///sdcard/rewind_results.html";
        getcurrip();
        //WEB_ROOT = new File("File:///sdcard")

    }
    public void openfile(View v)
    {
        Intent it = new Intent();
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.setType("*/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, "DEMO"),1001);
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            currFileURI = data.getData();
            File fd;
            try {
                fd = new File(FileChooser.getPath(this, currFileURI));
                tojson(fd.getPath());
                Log.i("Filepath", fd.toString());
            network ntw = new network();
            ntw.execute(fd);
            }
            catch(Exception e)
            {
                Log.i("fd", e.toString());
            }

        }}
        private void tojson(String inp)
        {
            ArrayList<String> ar = new ArrayList<>();
            Log.i("Eindoed",inp);
            ar.add(inp);
            JSONArray jr = new JSONArray(ar);
            writetofile(jr.toString());
        }
        private void writetofile(String inp)
        {
            File folder = Environment.getExternalStorageDirectory();
            File file = new File(folder,"Music/relayjs.json");
            file.delete();
            try {
                file.createNewFile();
                FileWriter wr = new FileWriter(file);
                wr.write("var paths = "+inp);
                wr.flush();
                wr.close();
            }
            catch (Exception e)
            {
                Log.e("writetofile",e.toString());
            }
        }
        private void getcurrip()
        {
            ip=Utils.getIPAddress(true);
            Log.i("IPA",ip);
            TextView tv = findViewById(R.id.textView6);
            tv.setText((ip+":53000"));
        }
}

