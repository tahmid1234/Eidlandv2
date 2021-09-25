package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.Viewer;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class ViewerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    PlacesViewHolder placesViewHolder = null;
    Context context;
    ArrayList<Viewer> countryInfoArrayList;


    public ViewerListAdapter(Context context, ArrayList<Viewer> cameraobject1s) {
        this.context = context;
        countryInfoArrayList = cameraobject1s;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleuser, parent, false);
        PlacesViewHolder placesViewHolder = new PlacesViewHolder(view, countryInfoArrayList);
        return placesViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        placesViewHolder = (PlacesViewHolder) holder;
        Glide.with(context).load(countryInfoArrayList.get(position).getPhotoUrl()).into(placesViewHolder.ivFamousPlace);
        String t = countryInfoArrayList.get(position).getName();
        placesViewHolder.UName.setText(t);
    }

    @Override
    public int getItemCount() {
        return countryInfoArrayList.size();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements com.eidland.auxilium.voice.only.adapter.PlacesViewHolder {

        View view;
        ImageView ivFamousPlace;
        TextView UName;
        ArrayList<Viewer> countryInfoArrayList;

        public PlacesViewHolder(View itemView, ArrayList<Viewer> countryInfoArrayList) {
            super(itemView);
            view = itemView;
            ivFamousPlace = view.findViewById(R.id.userimglist);
            UName = view.findViewById(R.id.usernamelist);
            this.countryInfoArrayList = countryInfoArrayList;
        }
    }
}
