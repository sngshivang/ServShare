package com.share.contrify.contrifyshare;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
import java.util.ArrayList;

public class fileselect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileselect);
        ar = new ArrayList<>();
        arf = new ArrayList<>();
        ntw = new network();
        al = new ArrayList<>();
        lst = findViewById(R.id.filelst);
        st = new studadap(this,al);
    }
    network ntw;
    Uri currFileURI;
    ArrayList<fieldsinfo> al;
    studadap st;
    fieldsinfo ifo;
    ListView lst;
    AsyncTask atsk;
    String fname=null,fpath=null,fattr=null;
    ArrayList<String> ar;
    ArrayList<File> arf;
    public void addfile(View v)
    {
        Intent it = new Intent();
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.setType("*/*");
        it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, "DEMO"),1001);
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        int cnt=0,ini=0;
        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            File fd;
            try {
                cnt=data.getClipData().getItemCount();
                //fd = new File(currFileURI.getPath());
                for (;ini<cnt;ini++) {
                    currFileURI = data.getClipData().getItemAt(ini).getUri();
                    fd = new File(FileChooser.getPath(this, currFileURI));
                    tojson(fd);
                    String pth = fd.getPath();
                    fname = findnme(pth);
                    fpath = pth;
                    ifo = new fieldsinfo(fname, "TEST", fpath);
                    st.add(ifo);
                    lst.setAdapter(st);
                    Log.i("Filepath", pth);
                }

            }
            catch(Exception e)
            {
                Log.i("fd", e.toString());
            }

        }}
    private void tojson(File inp)
    {
        arf.add(inp);
        Log.i("Eindoed",inp.getPath());
        ar.add(inp.toString());

    }
    public void ststart(View v)
    {
        if (ntw.getStatus()==AsyncTask.Status.RUNNING)
            stop();
        else
            svstart();

    }
    protected void svstart()
    {
        ImageView iv = findViewById(R.id.imageView7);
        iv.setImageResource(R.drawable.power_vect_on);
        JSONArray jr = new JSONArray(ar);
        writetofile(jr.toString());
        atsk=ntw.execute();
    }
    protected void stop()
    {
        atsk.cancel(true);
        ImageView iv = findViewById(R.id.imageView7);
        if (ntw.getStatus()!=AsyncTask.Status.RUNNING)
        iv.setImageResource(R.drawable.power_sel);
    }
    private void writetofile(String inp)
    {
        Log.i("WRITE",inp);
        File folder = Environment.getExternalStorageDirectory();
        File file = new File(folder,"Music/relayjs.json");
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
