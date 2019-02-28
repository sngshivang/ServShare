package com.share.contrify.contrifyshare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class fileselect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileselect);
        ar = new ArrayList<>();
        arf = new ArrayList<>();
        al = new ArrayList<>();
        iv = findViewById(R.id.imageView7);
        lst = findViewById(R.id.filelst);
        lst.setEmptyView(findViewById(R.id.empty_list_view));
        st = new studadap(this,al);
        dr = findViewById(R.id.drawer_layout);
        //nv = findViewById(R.id.nav_view);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        modimg();

    }
    private void modimg()
    {
        System.out.println("Called it!");
        if (sv_module.getstat())
            iv.setImageResource(R.drawable.power_sel_on);
        else
            iv.setImageResource(R.drawable.power_sel);
    }
    DrawerLayout dr;
    NavigationView nv;
    Uri currFileURI;
    ImageView iv;
    ArrayList<fieldsinfo> al;
    studadap st;
    fieldsinfo ifo;
    ListView lst;
    String fname=null,fpath=null,fattr=null;
    ArrayList<String> ar;
    ArrayList<File> arf;
    public void addfile(View v)
    {
        Intent it = new Intent();
        it.setType("*/*");
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it,"TEST"),1001);
    }
    public void opendrawer(View v)
    {
        dr.openDrawer(GravityCompat.START);
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode,data);
        int cnt=0,ini=0;
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            {
                if (data.getClipData() != null) {

                    File fd;
                    try {
                        cnt = data.getClipData().getItemCount();
                        //fd = new File(currFileURI.getPath());
                        for (; ini < cnt; ini++) {
                            currFileURI = data.getClipData().getItemAt(ini).getUri();
                            fd = new File(FileChooser.getPath(this, currFileURI));
                            tojson(fd);
                            String pth = fd.getPath();
                            fname = findnme(pth);
                            fpath = pth;
                            ifo = new fieldsinfo(fname, "TEST", fpath);
                            st.add(ifo);
                            lst.setAdapter(st);
                        }

                    } catch (Exception e) {
                        Log.i("fd", e.toString());
                    }
                }
                else if (data.getData()!=null)
                {
                    File fd;
                    try{
                        currFileURI=data.getData();
                        fd = new File(FileChooser.getPath(this,currFileURI));
                        tojson(fd);
                        String pth = fd.getPath();
                        fname = findnme(pth);
                        fpath = pth;
                        ifo = new fieldsinfo(fname, "TEST", fpath);
                        st.add(ifo);
                        lst.setAdapter(st);

                    }
                    catch (Exception e) {
                        Log.i("File Browser Intent", e.toString());
                    }
                }
            }

        }}
    private void tojson(File inp)
    {
        arf.add(inp);
        Log.i("Eindoed",inp.getPath());
        ar.add(inp.toString());
        JSONArray jr = new JSONArray(ar);
        writetofile(jr.toString());
    }
    public void svstart(View v)
    {
        if (sv_module.getstat())
            iv.setImageResource(R.drawable.power_sel);
        else
            iv.setImageResource(R.drawable.power_sel_on);
        sv_module.ststart();
    }
    protected void writetofile(String inp)
    {
        Log.i("WRITE",inp);
        File folder = Environment.getExternalStorageDirectory();
        File file = new File(folder,"bootstrap-4.0.0-dist/relayjs.json");
        BufferedWriter bw;
        String wrin = "var paths = "+inp;
        file.delete();
        try {
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));
            //FileWriter wr = new FileWriter(file);
            try {
                bw.write(wrin);
            }finally {
                bw.flush();
                bw.close();
            }
        }
        catch (Exception e)
        {
            Log.e("writetofile",e.toString());
        }
    }
    private String findnme(String inp)
    {
        int len = inp.length();
        String out="";
        for (int i=len-1;i>-1;i--)
        {
            if (inp.charAt(i)=='/')
                break;
            else
                out=inp.charAt(i)+out;

        }
        return out;
    }
    public void rmfile(View v)
    {
        for (fieldsinfo p:st.getBox())
        {
            if (p.chkbx)
            {
                rmfromalst(p.pos);
            }
        }
    }
    private void rmfromalst(int pos)
    {
        al.remove(pos);
        ar.remove(pos);
        st= new studadap(this,al);
        lst.setAdapter(st);
    }
}