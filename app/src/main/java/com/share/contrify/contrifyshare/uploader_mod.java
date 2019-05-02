package com.share.contrify.contrifyshare;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class uploader_mod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader_mod);
        dr = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        iv2= findViewById(R.id.imageView8);
        svs_tvu=findViewById(R.id.svst_wru);
        svs_tv=findViewById(R.id.svst_wr);
        iv = findViewById(R.id.imageView7);
        navstuff();
        dispval();
    }
    DrawerLayout dr;
    NavigationView nv;
    ImageView iv,iv2;
    TextView svs_tvu,svs_tv;
    String ip="";
    public void startser(View v)
    {
        if (uploadser.getstat())
        uploadser.ftpsstart();
        else
            uploadser.stopser();
    }
    private void navstuff()
    {
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem mt) {
                        mt.setChecked(true);
                        if (mt.getItemId()==R.id.nav_home)
                        {
                            Intent it = new Intent(uploader_mod.this,starter.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Intent it = new Intent(uploader_mod.this,setting.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_about)
                        {
                            Intent it = new Intent(uploader_mod.this,about.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_upl)
                        {
                            Intent it = new Intent(uploader_mod.this,MainActivity.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_dwn)
                        {
                            Intent it = new Intent(uploader_mod.this,uploader_mod.class);
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
    private void dispval()
    {
        String uname= universals.usrnme;
        uname="Username: "+uname;
        String pass = universals.spass;
        pass="Password :"+pass;
        TextView tv = findViewById(R.id.usnme);
        tv.setText(uname);
        tv = findViewById(R.id.pass);
        tv.setText(pass);
        tv = findViewById(R.id.ipa);
        ip=Utils.getIPAddress(true);
        if (!ip.equals(""))
            tv.setText((ip+":2221"));

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
    @Override
    protected void onResume()
    {
        super.onResume();
        dispval();
        modimg();
        modimg2();
    }

}
