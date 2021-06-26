package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.Viewer;


public class ViewerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    PlacesViewHolder placesViewHolder = null;
    Context context;
    ArrayList<Viewer> countryInfoArrayList;

    public ViewerAdapter(Context context, ArrayList<Viewer> cameraobject1s) {
        this.context = context;
        countryInfoArrayList = cameraobject1s;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singalsmalluser, parent, false);
        PlacesViewHolder placesViewHolder = new PlacesViewHolder(view);
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
    }

    @Override
    public int getItemCount() {
        return countryInfoArrayList.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder{

        View view;
        ImageView ivFamousPlace;

        public PlacesViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivFamousPlace = view.findViewById(R.id.img);
        }
    }


}
