package com.share.contrify.contrifyshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class fileselect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileselect);
        ar = new ArrayList<>();
        arf = new ArrayList<>();
    }
    Uri currFileURI;
    studadap st;
    ListView lst;
    String fname=null,fpath=null,fattr=null;
    ArrayList<String> ar;
    ArrayList<File> arf;
    public void addfile(View v)
    {
        ArrayList<fieldsinfo> al = new ArrayList<>();
        lst = findViewById(R.id.filelst);
        st = new studadap(this,al);
        Intent it = new Intent();
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.setType("*/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, "DEMO"),1001);
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            currFileURI = data.getData();
            File fd;
            try {
                fd = new File(FileChooser.getPath(this, currFileURI));
                tojson(fd);
                String pth = fd.getPath();
                fname=findnme(pth);
                fpath=pth;
                fieldsinfo ifo = new fieldsinfo(fname,"TEST",fpath);
                st.add(ifo);
                lst.setAdapter(st);
                Log.i("Filepath", pth);

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
    public void svstart(View v)
    {
        ImageView iv = findViewById(R.id.imageView7);
        iv.setImageResource(R.drawable.power_vect_on);
        JSONArray jr = new JSONArray(ar);
        writetofile(jr.toString());
        network ntw = new network();
        ntw.execute();

    }
    private void writetofile(String inp)
    {
        File folder = Environment.getExternalStorageDirectory();
        File file = new File(folder,"Music/relayjs.json");
        file.delete();
        try {
            file.createNewFile();
            FileWriter wr = new FileWriter(file);
            wr.write("var paths = "+inp);
            wr.flush();
            wr.close();
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
}
