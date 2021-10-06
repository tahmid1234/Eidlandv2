package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.Viewer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Adapterspinner extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Viewer> items;
    private final int mResource;

    public Adapterspinner(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List objects) {
        super(context,
                resource,0,
                objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView  (int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView offTypeTv = (TextView) view.findViewById(R.id.spinnertextid);


        Viewer offerData = items.get(position);

        offTypeTv.setText(offerData.getName());


        return view;
    }
}