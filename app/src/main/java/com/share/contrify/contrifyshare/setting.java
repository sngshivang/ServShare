package com.share.contrify.contrifyshare;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dr = findViewById(R.id.drawer_layout);
        svs_tv=findViewById(R.id.svst_wr);
        nv = findViewById(R.id.nav_view);
        iv = findViewById(R.id.imageView7);
        iv2= findViewById(R.id.imageView8);
        svs_tvu=findViewById(R.id.svst_wru);
        navstuff();
    }
    DrawerLayout dr;
    ImageView iv,iv2;
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
                            Intent it = new Intent(setting.this,starter.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Intent it = new Intent(setting.this,setting.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_about)
                        {
                            Intent it = new Intent(setting.this,about.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_upl)
                        {
                            Intent it = new Intent(setting.this,MainActivity.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_dwn)
                        {
                            Intent it = new Intent(setting.this,uploader_mod.class);
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
    public void chprt(View v)
    {
        if (sv_module.getstat())
            svstart(null);
        EditText et = findViewById(R.id.editport);
        String inp = et.getText().toString();
        int val = Integer.parseInt(inp);
        if (val>1023) {
            universals.chport(val);
            alerts("SUCCESS","The operation performed was successful");
        }
        else
            alerts("RESTRICTED PORT","Setting port below 1024 is not allowed by the Android SubSystem");

    }
    public void reset(View v)
    {
        AlertDialog.Builder abd = new AlertDialog.Builder(this);
        try {
            File fl = new File(this.getFilesDir(), "SYSFILE1");
            if (!fl.delete())
            {
                abd.setTitle("FAILURE");
                abd.setMessage("Reset operation was not successful. Please try again. If the problem persists, contact support");
                abd.show();
            }
            Intent it = new Intent(this,firstrun.class);
            startActivity(it);
        }
        catch (Exception e)
        {
            Log.e("settings",e.toString());
            abd.setTitle("FAILURE");
            abd.setMessage("Fatal Exception occurred. Contact Support");
            abd.show();

        }
    }
    public void chup(View v)
    {
        if (!uploadser.getstat())
            upstart(null);
        EditText ed = findViewById(R.id.editport1);
        EditText ed2 = findViewById(R.id.editport2);
        String un = ed.getText().toString();
        String ps = ed2.getText().toString();
        if (un.length()>4&&ps.length()>4) {
            universals.chusrpass(un, ps);
            alerts("SUCCESS","The operation performed was successful");
        }
        else
            alerts("SHORT LENGTH","The username or password is less than 5 character");
    }
    private void alerts(String tit,String msg)
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(tit);
        adb.setMessage(msg);
        adb.show();
    }
}
