package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.activity.LiveRoomActivity;
import com.eidland.auxilium.voice.only.model.Viewer;

import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Adapterspinner extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<Viewer> items;
    private List<String> names;
    private int mResource;
    private OnSpinnerClickListener onSpinnerClickListener;

    public Adapterspinner(@NonNull Context context, @LayoutRes int resource, @NonNull List objects, List onlyname, OnSpinnerClickListener onSpinnerClickListener) {
        super(context, resource, 0, onlyname);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        names = onlyname;
        this.onSpinnerClickListener = onSpinnerClickListener;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView offTypeTv = (TextView) view.findViewById(R.id.spinnertextid);
        //   Viewer offerData = items.get(position);
        offTypeTv.setText(names.get(position));
        offTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, convertView.toString(), Toast.LENGTH_SHORT).show();

                hideSpinnerDropDown(LiveRoomActivity.spinner);
                onSpinnerClickListener.onItemSelected(parent, view, position);
            }
        });
        return view;
    }

    public interface OnSpinnerClickListener {
        void onItemSelected(ViewGroup parent, View view, int pos);

    }
}