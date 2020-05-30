package com.share.contrify.contrifyshare;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class firstrun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstrun);
        Log.i("firstrun","Worksfine");
        if (reader())
        {
            setdefs(this);
            Intent it = new Intent(this,MainActivity.class);
            startActivity(it);
            finish();
        }
        if (ActivityCompat.checkSelfPermission(firstrun.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        perval);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    int perval;

    public void cpfl(View v)
    {
        AlertDialog.Builder abd = new AlertDialog.Builder(this);
        InputStream input=null;
        OutputStream output=null;
        try {
            input = this.getAssets().open("servshare_0D0D1.zip");
            String outPath = Environment.getExternalStorageDirectory()+"/servshare_0D0D1.zip";
            output = new FileOutputStream(outPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        }
        catch(Exception e)
        {
            Log.e("firstrun",e.toString());
        }
        finally {
            try{
                output.flush();
                output.close();
                input.close();
            }catch (Exception e)
            {
                Log.e("firstrun",e.toString());
                abd.setTitle("FAILURE");
                abd.setMessage("First run operations were not successful. Please try again. If the problem persists, contact support");
                abd.show();
            }
        }
        if (unpackZip(Environment.getExternalStorageDirectory().getPath()+"/","servshare_0D0D1.zip"))
        {
            abd.setTitle("SUCCESS");
            abd.setMessage("First run operations were successful");
            abd.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finalend();
                }
            });
            abd.show();
        }
        else
        {
            abd.setTitle("FAILURE");
            abd.setMessage("First run operations were not successful. Please try again. If the problem persists, contact support");
            abd.show();
        }
    }
    //peno's code--- whatever the heck that means!
    private boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }
            try {
                File fl = new File(Environment.getExternalStorageDirectory(), "servshare_0D0D1.zip");
                if (!fl.delete()) {
                    Log.i("firstrun", "ZIP DELETE FAILED");
                }
            }
            catch (Exception e)
            {
                Log.e("firstrun",e.toString());
            }
            zis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    private void finalend()
    {
        try{
            FileWriter fw = new FileWriter(new File(this.getFilesDir(),"SYSFILE1"));
            BufferedWriter out = new BufferedWriter(fw);
            out.write("FR_DISABLE");
            out.close();
        }catch (Exception e)
        {
            Log.e("firstrun",e.toString());
        }
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();

    }
    private boolean reader()
    {
        String fnl="";
        try {
            FileReader fr = new FileReader(new File(this.getFilesDir(), "SYSFILE1"));
            BufferedReader br = new BufferedReader(fr);
            String join;
            while ((join=br.readLine())!=null)
            {
                fnl+=join;
            }
        }catch (Exception e)
        {
            Log.e("firstrun",e.toString());
        }
        Log.i("reader",fnl);
        if (fnl.equals("FR_DISABLE")) {
            return true;
        }
        else {
            sysfile2cr();
            return false;
        }
    }
    protected void setdefs(Context ct)
    {
        try {
            String fnl="";
            File syslf = new File(ct.getFilesDir(), "SYSFILE2");
            FileReader fr = new FileReader(syslf);
            BufferedReader br = new BufferedReader(fr);
            String join;
            while ((join = br.readLine()) != null) {
                fnl += join;
            }
            JSONObject js = new JSONObject(fnl);
            String usrnme = js.getString("uplusr");
            String usrpwd = js.getString("uplpwd");
            String flp = js.getString("uplpath");
            int prt = Integer.parseInt(js.getString("dwnprt"));
            universals.chuplpth(new File(flp));
            universals.chusrpass(usrnme,usrpwd);
            universals.chport(prt);
        }
        catch (Exception e)
        {
            Log.e("setdefs", e.toString());
        }
    }
    private void sysfile2cr()
    {
        JSONObject js = new JSONObject();
        try {
            js.put("uplusr", "username");
            js.put("uplpwd", "password");
            js.put("dwnprt", "53000");
            js.put("uplpath", Environment.getExternalStorageDirectory().getPath());
            String out = js.toString();
            FileWriter fw = new FileWriter(new File(this.getFilesDir(),"SYSFILE2"));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(out);
            bw.close();
        }
        catch (Exception e)
        {
            Log.e("sysfile2cr",e.toString());
        }


    }
}
