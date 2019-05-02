package com.share.contrify.contrifyshare;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class firstrun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstrun);
        if (reader())
        {
            Intent it = new Intent(this,starter.class);
            startActivity(it);
        }
        dr = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        svs_tv = findViewById(R.id.svst_wr);
        iv = findViewById(R.id.imageView7);
        iv2= findViewById(R.id.imageView8);
        svs_tvu=findViewById(R.id.svst_wru);
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
        navstuff();
        modimg();
    }
    int perval;
    DrawerLayout dr;
    NavigationView nv;
    TextView svs_tv,svs_tvu;
    ImageView iv,iv2;
    private void navstuff()
    {
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem mt) {
                        mt.setChecked(true);
                        if (mt.getItemId()==R.id.nav_home)
                        {
                            Intent it = new Intent(firstrun.this,starter.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Intent it = new Intent(firstrun.this,setting.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_about)
                        {
                            Intent it = new Intent(firstrun.this,about.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_upl)
                        {
                            Intent it = new Intent(firstrun.this,MainActivity.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_dwn)
                        {
                            Intent it = new Intent(firstrun.this,uploader_mod.class);
                            startActivity(it);
                        }
                        dr.closeDrawers();
                        return true;
                    }
                });
    }
    public void opendrawer(View v)
    {
        dr.openDrawer(GravityCompat.START);
    }
    private void modimg()
    {
        if (sv_module.getstat()) {
            iv.setImageResource(R.drawable.send_main);
            svs_tv.setText(R.string.svst_wr1);
            svs_tv.setTextColor(Color.parseColor("#02F424"));
        }
        else{
            iv.setImageResource(R.drawable.send_main_off);
            svs_tv.setText(R.string.svst_wr2);
            svs_tv.setTextColor(Color.parseColor("#FF0000"));
        }
    }
    private void modimg2()
    {
        if (uploadser.getstat()) {
            iv2.setImageResource(R.drawable.receive_main_off);
            svs_tvu.setText(R.string.svst_wr2u);
            svs_tvu.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            iv2.setImageResource(R.drawable.receive_main);
            svs_tvu.setText(R.string.svst_wr1u);
            svs_tvu.setTextColor(Color.parseColor("#02F424"));
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        modimg();
        modimg2();
    }
    public void svstart(View v)
    {
        if (sv_module.getstat()){
            iv.setImageResource(R.drawable.send_main_off);
            svs_tv.setText(R.string.svst_wr2);
            svs_tv.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            iv.setImageResource(R.drawable.send_main);
            svs_tv.setText(R.string.svst_wr1);
            svs_tv.setTextColor(Color.parseColor("#02F424"));
        }
        sv_module.ststart();
    }
    public void upstart(View v)
    {
        if (uploadser.getstat()){
            iv2.setImageResource(R.drawable.receive_main);
            svs_tvu.setText(R.string.svst_wr1u);
            svs_tvu.setTextColor(Color.parseColor("#02F424"));
            uploadser.ftpsstart();
        }
        else{
            iv2.setImageResource(R.drawable.receive_main_off);
            svs_tvu.setText(R.string.svst_wr2u);
            svs_tvu.setTextColor(Color.parseColor("#FF0000"));
            uploadser.stopser();
        }
    }
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
        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);

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
        if (fnl.equals("FR_DISABLE"))
            return true;
        else
            return false;
    }
}
