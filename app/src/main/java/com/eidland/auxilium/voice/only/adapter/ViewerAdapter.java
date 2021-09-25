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
    private OnViewerClickListener onViewerClickListener;

    public ViewerAdapter(Context context, ArrayList<Viewer> cameraobject1s, OnViewerClickListener onViewerClickListener) {
        this.context = context;
        countryInfoArrayList = cameraobject1s;
        this.onViewerClickListener = onViewerClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singalsmalluser, parent, false);
        PlacesViewHolder placesViewHolder = new PlacesViewHolder(view, onViewerClickListener);
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

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        ImageView ivFamousPlace;
        OnViewerClickListener onViewerClickListener;

        public PlacesViewHolder(View itemView, OnViewerClickListener onViewerClickListener) {
            super(itemView);
            view = itemView;
            ivFamousPlace = view.findViewById(R.id.img);
            this.onViewerClickListener = onViewerClickListener;

        }

        @Override
        public void onClick(View view) {
            onViewerClickListener.onViewerClick(getAdapterPosition());
        }

    }

    public interface OnViewerClickListener{
        void onViewerClick(int adapterPosition);
    }

}