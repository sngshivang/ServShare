package com.share.contrify.contrifyshare;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fileselector.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fileselector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fileselector extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mainview;
    private uplfileinfo tup;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private studadap st;
    private fieldsinfo ifo;
    static ArrayList<uplfileinfo> upfildat = new ArrayList<>();
    private ListView lst;
    private static int itms = 0;
    private static ArrayList<String> ar = new ArrayList<>();
    ArrayList<File> arf;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fileselector() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fileselector.
     */
    // TODO: Rename and change types and number of parameters
    public static fileselector newInstance(String param1, String param2) {
        fileselector fragment = new fileselector();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainview  = inflater.inflate(R.layout.fragment_fileselector, container, false);
        lst = mainview.findViewById(R.id.filelst);
        lst.setEmptyView(mainview.findViewById(R.id.elv2));
        st = new studadap(getActivity(),universals.al);
        lst.setAdapter(st);
        listeners();
        directfilelist();
        Button bt = mainview.findViewById(R.id.startser);
        String temp;
        if (sv_module.getstat())
            temp = "STOP SERVER";
        else
            temp = "START SERVER";
        bt.setText(temp);
        Log.i("fragmentcreate","fileselector");
        return mainview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void triggerstuff(int val);
    }
    private void listeners()
    {
        Button b1 = mainview.findViewById(R.id.addfile);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addfile();
            }
        });
        Button b2 = mainview.findViewById(R.id.rmfile);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rmfile();
            }
        });
        Button b3 = mainview.findViewById(R.id.startser);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startser();
            }
        });
    }
    private void addfile()
    {
        Intent it = new Intent();
        it.setType("*/*");
        it.addCategory(Intent.CATEGORY_OPENABLE);
        it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        it.setAction(Intent.ACTION_OPEN_DOCUMENT);
        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it,""),1001);
    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode,data);
        int cnt=0,ini=0;
        Uri currFileURI;
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            {
                if (data.getClipData() != null) {

                    try {
                        cnt = data.getClipData().getItemCount();
                        for (; ini < cnt; ini++) {
                            currFileURI = data.getClipData().getItemAt(ini).getUri();
                            universals.crcontext(getActivity());
                            universals.crunivuri(currFileURI);
                            Cursor cr = getActivity().getContentResolver().query(currFileURI, null, null, null, null, null);
                            String nx="", sx="";
                            try {
                                if (cr != null && cr.moveToFirst()) {
                                    nx = cr.getString(cr.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                    sx = cr.getString(cr.getColumnIndex(OpenableColumns.SIZE));
                                }
                                cr.close();
                            } catch (Exception e)
                            {
                                Log.e("Cursor:",e.toString());
                            }
                            tup = new uplfileinfo(currFileURI, sx);
                            upfildat.add(tup);
                            String net = "/ssq"+itms+"/"+nx;
                            itms++;
                            Log.i("TestURI",net);
                            float dc = Float.parseFloat(sx);
                            dc = (dc/1024)/1024;
                            String sdc = df.format(dc);
                            sdc += " MB";
                            tojson(net);
                            ifo = new fieldsinfo(nx, "", sdc);
                            st.add(ifo);
                            lst.setAdapter(st);
                        }

                    } catch (Exception e) {
                        Log.i("fd", e.toString());
                    }
                }
                else if (data.getData()!=null)
                {
                    try{
                        currFileURI=data.getData();
                        universals.crcontext(getActivity());
                        universals.crunivuri(currFileURI);
                        Cursor cr = getActivity().getContentResolver().query(currFileURI, null, null, null, null, null);
                        String nx="", sx="";
                        try {
                            if (cr != null && cr.moveToFirst()) {
                                nx = cr.getString(cr.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                sx = cr.getString(cr.getColumnIndex(OpenableColumns.SIZE));
                            }
                            cr.close();
                        } catch (Exception e)
                        {
                            Log.e("Cursor:",e.toString());
                        }
                        tup = new uplfileinfo(currFileURI, sx);
                        upfildat.add(tup);
                        String net = "/ssq"+itms+"/"+nx;
                        itms++;
                        Log.i("TestURI",net);
                        float dc = Float.parseFloat(sx);
                        dc = (dc/1024)/1024;
                        String sdc = df.format(dc);
                        sdc += " MB";
                        tojson(net);
                        ifo = new fieldsinfo(nx, "", sdc);
                        st.add(ifo);
                        lst.setAdapter(st);

                    }
                    catch (Exception e) {
                        Log.i("File Browser Intent", e.toString());
                    }
                }
            }

        }}
    private void tojson(String inp)
    {
        //arf.add(inp);
        //Log.i("Eindoed",inp.getPath());
        ar.add(inp);
        JSONArray jr = new JSONArray(ar);
        writetofile(jr.toString());
    }
    private void writetofile(String inp)
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
    private void rmfile()
    {
        Vector<Integer> vct = new Vector<>();
        for (fieldsinfo p:st.getBox())
        {
            if (p.chkbx)
            {
                vct.add(p.pos);
            }
        }
        rmfromalst(vct);
    }
    private void rmfromalst(Vector<Integer> pos)
    {
        Collections.reverse(pos);
        int posi;
        Iterator<Integer> it = pos.iterator();
        while (it.hasNext()) {
            posi = it.next();
            universals.al.remove(posi);
            ar.remove(posi);
        }
        st= new studadap(getContext(),universals.al);
        lst.setAdapter(st);
        JSONArray jr = new JSONArray(ar);
        writetofile(jr.toString());
    }
    private void directfilelist()
    {
        try{
        if (universals.directtrans == 2) {
            ar.clear();
            Uri currFileURI = universals.singtrans;
            universals.crcontext(getActivity());
            universals.crunivuri(currFileURI);
            Cursor cr = getActivity().getContentResolver().query(currFileURI, null, null, null, null, null);
            String nx = "", sx = "";
            try {
                if (cr != null && cr.moveToFirst()) {
                    nx = cr.getString(cr.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    sx = cr.getString(cr.getColumnIndex(OpenableColumns.SIZE));
                }
                cr.close();
            } catch (Exception e) {
                Log.e("Cursor:", e.toString());
            }
            tup = new uplfileinfo(currFileURI, sx);
            upfildat.add(tup);
            String net = "/ssq" + itms + "/" + nx;
            itms++;
            Log.i("TestURI", net);
            float dc = Float.parseFloat(sx);
            dc = (dc / 1024) / 1024;
            String sdc = df.format(dc);
            sdc += " MB";
            tojson(net);
            ifo = new fieldsinfo(nx, "", sdc);
            st.add(ifo);
            lst.setAdapter(st);
            universals.directtrans = 0;
        }
        else if (universals.directtrans == 1)
        {
            ar.clear();
            int cnt = universals.multitrans.size();
            for (int ini= 0; ini < cnt; ini++) {
                Uri currFileURI = universals.multitrans.get(ini);
                universals.crcontext(getActivity());
                universals.crunivuri(currFileURI);
                Cursor cr = getActivity().getContentResolver().query(currFileURI, null, null, null, null, null);
                String nx="", sx="";
                try {
                    if (cr != null && cr.moveToFirst()) {
                        nx = cr.getString(cr.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        sx = cr.getString(cr.getColumnIndex(OpenableColumns.SIZE));
                    }
                    cr.close();
                } catch (Exception e)
                {
                    Log.e("Cursor:",e.toString());
                }
                tup = new uplfileinfo(currFileURI, sx);
                upfildat.add(tup);
                String net = "/ssq"+itms+"/"+nx;
                itms++;
                Log.i("TestURI",net);
                float dc = Float.parseFloat(sx);
                dc = (dc/1024)/1024;
                String sdc = df.format(dc);
                sdc += " MB";
                tojson(net);
                ifo = new fieldsinfo(nx, "", sdc);
                st.add(ifo);
                lst.setAdapter(st);
                universals.directtrans = 0;
            }
        }
        }
        catch (Exception e)
        {
            Log.e("DIRFILE", e.toString());
        }

    }
    private void startser()
    {
        Button bt = mainview.findViewById(R.id.startser);
        String temp;
        if (!sv_module.getstat()) {
            mListener.triggerstuff(1);
            AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
            adb.setTitle("SERVER STARTED");
            adb.setMessage("Server has been started. Please go to back to previous screen to know details of URL to download the files in the host.");
            adb.setCancelable(false);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Navigation.findNavController(mainview).navigate(R.id.action_fileselector_to_fileupload);
                }
            });
            adb.create();
            adb.show();
            temp = "STOP SERVER";
        }
        else {
            mListener.triggerstuff(1);
            temp = "START SERVER";
        }
            bt.setText(temp);


    }
}
