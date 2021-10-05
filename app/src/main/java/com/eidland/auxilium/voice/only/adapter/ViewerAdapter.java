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
    private Context context;
    private OnViwersClickListener onViwersClickListener;
    ArrayList<Viewer> countryInfoArrayList;

    public ViewerAdapter(Context context,OnViwersClickListener onViwersClickListener1, ArrayList<Viewer> cameraobject1s) {
        this.context = context;
        this.onViwersClickListener=onViwersClickListener1;
        countryInfoArrayList = cameraobject1s;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlacesViewHolder (LayoutInflater.from(context).inflate(R.layout.singalsmalluser, parent, false), onViwersClickListener);

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

     class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        ImageView ivFamousPlace;
        OnViwersClickListener onViwersClickListener;

        public PlacesViewHolder(View itemView, OnViwersClickListener onViwersClickListener2) {
            super(itemView);
            view = itemView;
            ivFamousPlace = view.findViewById(R.id.img);
            this.onViwersClickListener=onViwersClickListener2;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           int pos=this.getLayoutPosition();
           onViwersClickListener.onViewersClick(getAdapterPosition(),countryInfoArrayList.get(pos).name,countryInfoArrayList.get(pos).photo, countryInfoArrayList.get(pos).recievedCoins);
        }
    }
    public interface OnViwersClickListener {
        void onViewersClick(int position, String name, String photo, String recievedCoin);
    }

}