package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.model.AnimationItem;
import com.eidland.auxilium.voice.only.model.Comment;

import java.net.URI;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;


public class AdapterComment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ItemClickListener1 itemClickListener1;
    PlacesViewHolder placesViewHolder = null;
    Context context;
    ArrayList<Comment> countryInfoArrayList;
    URI phURI;

    public AdapterComment(Context context, ArrayList<Comment> cameraobject1s, ItemClickListener1 itemClickListener1) {
        this.context = context;
        countryInfoArrayList = cameraobject1s;
        this.itemClickListener1 = itemClickListener1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecmnt, parent, false);
        PlacesViewHolder placesViewHolder = new PlacesViewHolder(view, itemClickListener1);
        return placesViewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        placesViewHolder = (PlacesViewHolder) holder;
        placesViewHolder.txtuser.setText(countryInfoArrayList.get(position).getName());
        System.out.printf("comment dekhi "+countryInfoArrayList.get(position).getComment()+" nah");
        placesViewHolder.txtcmnt.setText(countryInfoArrayList.get(position).getComment().trim());

        Glide.with(this.context).load(countryInfoArrayList.get(position).getUserphoto()).into(placesViewHolder.userImg);
            if(countryInfoArrayList.get(position).isHasimg()) {
                placesViewHolder.gift.setVisibility(View.VISIBLE);
                //placesViewHolder.txtcount.setVisibility(View.VISIBLE);
                //placesViewHolder.txtcount.setText("");

                for (AnimationItem animationItem :
                        ConstantApp.animationItems()) {
                    if (animationItem.name.equals(countryInfoArrayList.get(position).getImgid())) {
                        placesViewHolder.gift.setBackgroundResource(animationItem.svgIconId);
                    }
                }
            }
            else {

                placesViewHolder.gift.setVisibility(View.GONE);
               // placesViewHolder.txtcount.setVisibility(View.GONE);
            }
//            placesViewHolder.gift.setVisibility(View.GONE);
//            placesViewHolder.txtcount.setVisibility(View.GONE);
        }


//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                //  Toast.makeText(context, "" + famousPlacesArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
//                showInterstitial(position);
//
//

    @Override
    public int getItemCount() {
        return countryInfoArrayList.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        TextView txtcmnt;
        TextView txtuser, txtcount;
        CircleImageView userImg;
        GifImageView gift;

        ItemClickListener1 itemClickListener1;

        public PlacesViewHolder(View itemView, ItemClickListener1 listener1) {
            super(itemView);
            view = itemView;
            itemClickListener1 = listener1;

            //txtcount = view.findViewById(R.id.txtcount);
            gift = view.findViewById(R.id.imggift);
            userImg = view.findViewById(R.id.userimgcomment);
            txtcmnt = view.findViewById(R.id.cmnt);
            txtuser = view.findViewById(R.id.user);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClickListener1.onPositionClicked(view, getAdapterPosition());
        }
    }


}
