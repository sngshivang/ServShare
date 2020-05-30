package com.share.contrify.contrifyshare;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements aboutfrag.OnFragmentInteractionListener, filedownload.OnFragmentInteractionListener, fileselector.OnFragmentInteractionListener, fileupload.OnFragmentInteractionListener, mainselector.OnFragmentInteractionListener, settings.OnFragmentInteractionListener {

    int perval = 1;
    public String ip;
    NavController navController;
    NavGraph ng;
    AdView mAdView;
    AppBarConfiguration apbr;
    Toolbar mytoolbar;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Teslacoil");
        dl = findViewById(R.id.drawer_layout);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        ng = navController.getGraph();
        apbr = new AppBarConfiguration.Builder(ng).setDrawerLayout(dl).build();
        mytoolbar = findViewById(R.id.my_toolbar);
        //mytoolbar.setContentInsetsAbsolute(0, 0);
        mytoolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(mytoolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(null);
        //ab.setDisplayHomeAsUpEnabled(false);
        //ab.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.hamburg_icon));
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().hide();
        //ab.setLogo(R.drawable.sv_logo2);
        //ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        //Toolbar toolbar = findViewById(R.id.my_toolbar);
        navView = findViewById(R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this, navController, dl);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(mytoolbar, navController, apbr);
        svs_tv = findViewById(R.id.svst_wr);
        navstuff();
        //iv2= findViewById(R.id.imageView9);
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
        Intent it2 = getIntent();
        String action = it2.getAction();
        String type = it2.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null)
        {
            handleunitfile(it2);
        }
        else if (Intent.ACTION_OPEN_DOCUMENT.equals(action) && type != null)
        {
            handleunitfile(it2);
        }
        if (Intent.ACTION_SEND_MULTIPLE.equals(action)  && type != null) {
            handlefiles(it2);
        }
        checkdirectsend();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
        catch (Exception e)
        {
            Log.e("ADERROR", e.toString());
        }
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded()
            {
                Log.i("AD","addloaded");
                layoutmanip2();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("ADFAIL",String.valueOf(errorCode));
            }

        });


    }
    DrawerLayout dl;
    Menu umenu;
    TextView svs_tv,svs_tvu;
    void handlefiles(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            universals.multitrans = imageUris;
            universals.directtrans = 1;
        }
    }
    void handleunitfile(Intent intent) {
        Uri imageUri =  intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            universals.singtrans = imageUri;
            universals.directtrans = 2;
        }
    }
    private void navstuff()
    {
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem mt) {
                        if (mt.getItemId()==R.id.nav_home)
                        {
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.mainselector);

                        }
                        else if (mt.getItemId()==R.id.nav_set)
                        {
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.settings);

                        }
                        else if (mt.getItemId()==R.id.nav_about)
                        {
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.aboutfrag);

                        }
                        else if (mt.getItemId()==R.id.nav_upl)
                        {
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.fileupload);

                        }
                        else if (mt.getItemId()==R.id.nav_dwn)
                        {
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.filedownload);

                        }

                        dl.closeDrawers();
                        return true;
                    }
                });
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
        if (umenu!=null) {
            if (sv_module.getstat()) {
                umenu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.send_main));
                svs_tv.setText(R.string.svst_wr1);
                svs_tv.setTextColor(Color.parseColor("#02F424"));
            } else {
                umenu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.send_main_off));
                svs_tv.setText(R.string.svst_wr2);
                svs_tv.setTextColor(Color.parseColor("#FF0000"));
            }
        }
    }
    private void modimg2()
    {
        if (umenu!=null) {
            if (uploadser.getstat()) {
                umenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.receive_main_off));
                svs_tvu.setText(R.string.svst_wr2u);
                svs_tvu.setTextColor(Color.parseColor("#FF0000"));
            } else {
                umenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.receive_main));
                svs_tvu.setText(R.string.svst_wr1u);
                svs_tvu.setTextColor(Color.parseColor("#02F424"));
            }
        }
    }
    private void svstart()
    {
        if (sv_module.getstat()){
            umenu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.send_main_off));
            svs_tv.setText(R.string.svst_wr2);
            svs_tv.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            umenu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.send_main));
            svs_tv.setText(R.string.svst_wr1);
            svs_tv.setTextColor(Color.parseColor("#02F424"));
        }
        sv_module.ststart();
    }
    private void upstart()
    {
        if (uploadser.getstat()){
            umenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.receive_main));
            svs_tvu.setText(R.string.svst_wr1u);
            svs_tvu.setTextColor(Color.parseColor("#02F424"));
            uploadser.ftpsstart();
        }
        else{
            umenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.receive_main_off));
            svs_tvu.setText(R.string.svst_wr2u);
            svs_tvu.setTextColor(Color.parseColor("#FF0000"));
            uploadser.stopser();
        }
    }
    private void checkdirectsend()
    {
        if (universals.directtrans != 0)
        {
            universals.al.clear();
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
                navController.navigate(R.id.action_mainselector_to_fileupload);

            }
            else {
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("ERROR!");
                adb.setMessage("First run operations have not been completed yet. Please start this application direct from your menu to complete the first run procedure.\nThis app will exit now");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);

                    }
                });
                adb.setCancelable(false);
                adb.create();
                adb.show();
            }
        }
    }
    /*public void upst(View v)
    {
        Intent it = new Intent(this, uploader_mod.class);
        startActivity(it);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.receive:
                upstart();
                return super.onOptionsItemSelected(item);
            case R.id.send:
                svstart();
                return super.onOptionsItemSelected(item);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        umenu = menu;
        modimg();
        modimg2();
        //triggerstuff();
        return true;
    }
    @Override
    public void triggerstuff(int inp)
    {
        Log.i("trigger",String.valueOf(inp));
        if (inp == 1)
        {
            svstart();
        }
        else if (inp == 2)
        {
            upstart();
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    private void layoutmanip2()
    {
        final ConstraintLayout dl = findViewById(R.id.content_frame);
        dl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()  {
            @Override
            public void onGlobalLayout() {
                int hei = dl.getMeasuredHeight();
                //dl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //final TypedArray styledAttributes = MainActivity.this.getTheme().obtainStyledAttributes(
                       // new int[]{android.R.attr.actionBarSize});
                //final int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
                //styledAttributes.recycle();
                int mActionBarSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());
                int ad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                final int net = hei - mActionBarSize - ad;
                final FragmentContainerView ft = findViewById(R.id.nav_host_fragment);
                ft.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()  {
                    @Override
                    public void onGlobalLayout() {
                        int hei = ft.getHeight();
                        ft.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ConstraintLayout.LayoutParams fl = (ConstraintLayout.LayoutParams) ft.getLayoutParams();
                        fl.height = net;
                        ft.setLayoutParams(fl);
                    }});


            }
        });
    }

}
