package com.share.contrify.contrifyshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mainview;
    private firstrun fr;
    private AlertDialog.Builder adb;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settings.
     */
    // TODO: Rename and change types and number of parameters
    public static settings newInstance(String param1, String param2) {
        settings fragment = new settings();
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
        mainview = inflater.inflate(R.layout.fragment_settings, container, false);
        setpathstat(universals.uplpth.getPath());
        listeners();
        fr = new firstrun();
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
        Button b1 = mainview.findViewById(R.id.chprt);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chprt();
            }
        });
        Button b2 = mainview.findViewById(R.id.chunp);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chup();
            }
        });
        Button b3 = mainview.findViewById(R.id.resetapp);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

    }
    private void chprt()
    {
        adb = new AlertDialog.Builder(getContext());
        if (sv_module.getstat()) {
            mListener.triggerstuff(1);
        }
        EditText et = mainview.findViewById(R.id.editport);
        String inp = et.getText().toString();
        int val = Integer.parseInt(inp);
        if (val>1023) {
            try {
                String fnl = readpref();
                JSONObject js = new JSONObject(fnl);
                js.put("dwnprt",inp);
                String out = js.toString();
                writechanges(out);
            }
            catch (Exception e)
            {
                Log.e("chngpath", e.toString());
            }
            fr.setdefs(getContext());
            alerts("SUCCESS","The operation performed was successful", adb);
        }
        else
            alerts("RESTRICTED PORT","Setting port below 1024 is not allowed by the Android SubSystem", adb);

    }
    private void reset()
    {
        adb = new AlertDialog.Builder(getContext());
        try {
            File fl = new File(getActivity().getFilesDir(), "SYSFILE1");
            if (!fl.delete())
            {
                adb.setTitle("FAILURE");
                adb.setMessage("Reset operation was not successful. Please try again. If the problem persists, contact support");
                adb.show();
            }
            Intent it = new Intent(getContext(),firstrun.class);
            startActivity(it);
        }
        catch (Exception e)
        {
            Log.e("settings",e.toString());
            adb.setTitle("FAILURE");
            adb.setMessage("Fatal Exception occurred. Contact Support");
            adb.show();

        }
    }
    private void chup()
    {
        adb = new AlertDialog.Builder(getContext());
        if (!uploadser.getstat()) {
            mListener.triggerstuff(2);
        }
        EditText ed = mainview.findViewById(R.id.editport1);
        EditText ed2 = mainview.findViewById(R.id.editport2);
        String un = ed.getText().toString();
        String ps = ed2.getText().toString();
        if (un.length()>4&&ps.length()>4) {
            try {
                String fnl = readpref();
                JSONObject js = new JSONObject(fnl);
                js.put("uplusr",un);
                js.put("uplpwd",ps);
                String out = js.toString();
                writechanges(out);
            }
            catch (Exception e)
            {
                Log.e("chngpath", e.toString());
            }
            fr.setdefs(getContext());
            alerts("SUCCESS","The operation performed was successful", adb);
        }
        else
            alerts("SHORT LENGTH","The username or password is less than 5 character", adb);
    }
    private void alerts(String tit,String msg, AlertDialog.Builder adb)
    {
        adb = new AlertDialog.Builder(getContext());
        adb.setTitle(tit);
        adb.setMessage(msg);
        adb.show();
    }

    private void moduplpath()
    {
        Intent it = new Intent();
        //it.setType("file/*");
        it.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(it, 1001);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri ur = data.getData();
        String fl = FileUtil.getFullPathFromTreeUri(ur,getActivity());
        Log.i("flpath", fl);
        try {
            String fnl = readpref();
            JSONObject js = new JSONObject(fnl);
            js.put("uplpath",fl);
            String out = js.toString();
            writechanges(out);
        }
        catch (Exception e)
        {
            Log.e("chngpath", e.toString());
        }
        fr.setdefs(getContext());
        //TODO handle your request here
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void writechanges(String out)
    {
        try {
            File syslf = new File(getActivity().getFilesDir(), "SYSFILE2");
            syslf.delete();
            FileWriter fw = new FileWriter(syslf);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(out);
            bw.close();
        }
        catch(Exception e)
        {
            Log.e("writechanges",e.toString());
        }
    }
    private String readpref()
    {
        String fnl  ="";
        try {

            File syslf = new File(getActivity().getFilesDir(), "SYSFILE2");
            FileReader fr = new FileReader(syslf);
            BufferedReader br = new BufferedReader(fr);
            String join;
            while ((join = br.readLine()) != null) {
                fnl += join;
            }
        }
        catch (Exception e)
        {
            Log.e("readpref",e.toString());
        }
        return fnl;
    }
    private void setpathstat(String inp)
    {
        TextView tv = mainview.findViewById(R.id.textView9);
        tv.setText(inp);
    }
}
