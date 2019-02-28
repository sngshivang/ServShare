package com.share.contrify.contrifyshare;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dr = findViewById(R.id.drawer_layout);
        nv = findViewById(R.id.nav_view);
        iv = findViewById(R.id.imageView7);
        navstuff();
    }
    DrawerLayout dr;
    ImageView iv;
    NavigationView nv;
    private void navstuff()
    {
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem mt) {
                        mt.setChecked(true);
                        if (mt.getItemId()==R.id.nav_home)
                        {
                            Intent it = new Intent(setting.this,MainActivity.class);
                            startActivity(it);
                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Intent it = new Intent(setting.this,setting.class);
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

    }
    private void modimg()
    {
        if (sv_module.getstat())
            iv.setImageResource(R.drawable.power_sel_on);
        else
            iv.setImageResource(R.drawable.power_sel);
    }
}
