package com.share.contrify.contrifyshare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.log4j.chainsaw.Main;
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
    ImageView iv,iv2;
    public String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dr = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        svs_tv = findViewById(R.id.svst_wr);
        navstuff();
        iv2= findViewById(R.id.imageView9);
        svs_tvu=findViewById(R.id.svst_wru);
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
        iv = findViewById(R.id.imageView7);
        getcurrip();

    }
    DrawerLayout dr;
    NavigationView nv;
    TextView svs_tv,svs_tvu;
    private void navstuff()
    {
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem mt) {
                        mt.setChecked(true);
                        if (mt.getItemId()==R.id.nav_home)
                        {
                            Intent it = new Intent(MainActivity.this,starter.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Intent it = new Intent(MainActivity.this,setting.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_about)
                        {
                            Intent it = new Intent(MainActivity.this,about.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_upl)
                        {
                            Intent it = new Intent(MainActivity.this,MainActivity.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_dwn)
                        {
                            Intent it = new Intent(MainActivity.this,uploader_mod.class);
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
    protected void getcurrip()
    {
        ip=Utils.getIPAddress(true);
        TextView tv = findViewById(R.id.textView8);
        if (ip.equals(""))
            tv.setText("NOT CONNECTED");
        else
            tv.setText((ip+":"+universals.port));
        Log.i("IPA",ip);


    }
    @Override
    protected void onResume()
    {
        super.onResume();
        getcurrip();
        modimg();
        modimg2();

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
    public void addfileit(View v)
    {
        Intent it = new Intent(this,fileselect.class);
        startActivity(it);
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
    public void upst(View v)
    {
        Intent it = new Intent(this, uploader_mod.class);
        startActivity(it);
    }
}
