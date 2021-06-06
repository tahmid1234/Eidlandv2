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

        ItemClickListener1 itemClickListener1;

        PlacesViewHolder placesViewHolder=null;
    Context context;
    ArrayList<Viewer> countryInfoArrayList;

    public ViewerAdapter(Context context, ArrayList<Viewer> cameraobject1s, ItemClickListener1 itemClickListener1) {
        this.context = context;
        countryInfoArrayList = cameraobject1s;
        this.itemClickListener1=itemClickListener1;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;


            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singalsmalluser, parent, false);

            PlacesViewHolder placesViewHolder = new PlacesViewHolder(view,itemClickListener1);


            return placesViewHolder;



    }

    @Override
    public int getItemViewType(int position) {


        return position;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            placesViewHolder=(PlacesViewHolder) holder;
        Glide.with(context).load( countryInfoArrayList.get(position).getPhotoUrl()).into(placesViewHolder.ivFamousPlace);


    }

    @Override
    public int getItemCount() {
        return countryInfoArrayList.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        ImageView ivFamousPlace;
        ImageView superimg;
        ItemClickListener1 itemClickListener1;

        public PlacesViewHolder(View itemView, ItemClickListener1 listener1) {
            super(itemView);
            itemClickListener1=listener1;
            view = itemView;

            ivFamousPlace = view.findViewById(R.id.img);
            superimg = view.findViewById(R.id.supr);
          view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
                itemClickListener1.onPositionClicked(view,getAdapterPosition());


        }

    }



}
