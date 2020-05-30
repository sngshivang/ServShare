package com.share.contrify.contrifyshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fileupload.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fileupload#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fileupload extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mainview;
    private Handler task;
    private int ri;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fileupload() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fileupload.
     */
    // TODO: Rename and change types and number of parameters
    public static fileupload newInstance(String param1, String param2) {
        fileupload fragment = new fileupload();
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
        mainview = inflater.inflate(R.layout.fragment_fileupload, container, false);
        listeners();
        getcurrip();
        if (universals.directtrans != 0)
        {
            ri = 1;
            task = new Handler(Looper.getMainLooper());
            task.postDelayed(newexp, 1000);
        }
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
    private void getcurrip()
    {
        String ip=Utils.getIPAddress(true);
        TextView tv = mainview.findViewById(R.id.textView8);
        String txt = "NOT CONNECTED";
        if (ip.equals(""))
            tv.setText(txt);
        else
            tv.setText((ip+":"+universals.port));
        Log.i("IPA",ip);
    }
    private void listeners()
    {
        TextView tv = mainview.findViewById(R.id.textView3);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(mainview).navigate(R.id.action_fileupload_to_fileselector);
            }
        });
        TextView tv2 = mainview.findViewById(R.id.textView5);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.triggerstuff(1);
            }
        });
    }
    Runnable newexp = new Runnable() {
        @Override
        public void run() {
            waitend(ri--);
        }
    };
    private void waitend(int cnt)
    {
        if (cnt == 0)
        {
            Navigation.findNavController(mainview).navigate(R.id.action_fileupload_to_fileselector);
        }
        else
        {
            task.postDelayed(newexp, 1000);
        }
    }

}
