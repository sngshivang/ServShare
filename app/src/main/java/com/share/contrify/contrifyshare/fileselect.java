package com.share.contrify.contrifyshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

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
        arf = new ArrayList<>();
        iv = findViewById(R.id.imageView7);
        lst = findViewById(R.id.filelst);
        svs_tv = findViewById(R.id.svst_wr);
        lst.setEmptyView(findViewById(R.id.elv2));
        st = new studadap(this,al);
        dr = findViewById(R.id.drawer_layout);
        iv2= findViewById(R.id.imageView8);
        svs_tvu=findViewById(R.id.svst_wru);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        modimg();
        modimg2();
        lst = findViewById(R.id.filelst);
        st = new studadap(this,al);
        lst.setAdapter(st);

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
    DrawerLayout dr;
    NavigationView nv;
    Uri currFileURI;
    TextView svs_tv,svs_tvu;
    ImageView iv,iv2;
    static ArrayList<fieldsinfo> al = new ArrayList<>();
    studadap st;
    fieldsinfo ifo;
    ListView lst;
    String fname=null,fpath=null,fattr=null;
    static ArrayList<String> ar = new ArrayList<>();
    ArrayList<File> arf;
    public void addfile(View v)
    {
        Intent it = new Intent();
        it.setType("*/*");
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it,""),1001);
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
                            ifo = new fieldsinfo(fname, "", fpath);
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
                        ifo = new fieldsinfo(fname, "", fpath);
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
    protected void writetofile(String inp)
    {
        Log.i("WRITE",inp);
        File folder = Environment.getExternalStorageDirectory();
        File file = new File(folder,"servshare_data/relayjs.json");
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
        JSONArray jr = new JSONArray(ar);
        writetofile(jr.toString());
    }
    public void getip(View v) {
        AlertDialog.Builder arb = new AlertDialog.Builder(this);
        arb.setTitle("IP Address");
        arb.setMessage(Utils.getIPAddress(true));
        arb.show();
    }
}