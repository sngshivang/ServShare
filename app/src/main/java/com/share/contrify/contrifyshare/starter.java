package com.share.contrify.contrifyshare;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class starter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        dr = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        svs_tv = findViewById(R.id.svst_wr);
        iv2= findViewById(R.id.imageView8);
        iv = findViewById(R.id.imageView7);
        svs_tvu=findViewById(R.id.svst_wru);
        navstuff();
        modimg();

    }
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
                            Intent it = new Intent(starter.this,starter.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Intent it = new Intent(starter.this,setting.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_about)
                        {
                            Intent it = new Intent(starter.this,about.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_upl)
                        {
                            Intent it = new Intent(starter.this,MainActivity.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_dwn)
                        {
                            Intent it = new Intent(starter.this,uploader_mod.class);
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
        Intent it = new Intent(this, uploader_mod.class);
        startActivity(it);
    }
    public void dwnfl(View v)
    {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
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
}
