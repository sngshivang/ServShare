package com.share.contrify.contrifyshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class studadap extends ArrayAdapter<fieldsinfo> {
    public studadap(Context context, ArrayList<fieldsinfo> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        fieldsinfo user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dispfile, parent, false);
        }
        // Lookup view for data population
        TextView flname =  convertView.findViewById(R.id.filenme);
        TextView flpath =  convertView.findViewById(R.id.filepath);
        TextView flatr = convertView.findViewById(R.id.fileattr);
        // Populate the data into the template view using the data object
        flname.setText(user.name);
        flpath.setText(user.dig);
        flatr.setText(user.inti);
        // Return the completed view to render on screen
        return convertView;
    }
}