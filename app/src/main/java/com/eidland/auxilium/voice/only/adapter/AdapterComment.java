package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.Comment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;


public class AdapterComment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ItemClickListener1 itemClickListener1;
    PlacesViewHolder placesViewHolder=null;
    Context context;
    ArrayList<Comment> countryInfoArrayList;
    URI phURI;
    public AdapterComment(Context context, ArrayList<Comment> cameraobject1s, ItemClickListener1 itemClickListener1) {
        this.context = context;
        countryInfoArrayList = cameraobject1s;
        this.itemClickListener1=itemClickListener1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecmnt, parent, false);
            PlacesViewHolder placesViewHolder = new PlacesViewHolder(view,itemClickListener1);
            return placesViewHolder;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            placesViewHolder = (PlacesViewHolder) holder;
            placesViewHolder.txtuser.setText( countryInfoArrayList.get(position).getName());
            placesViewHolder.txtcmnt.setText( countryInfoArrayList.get(position).getComment());

        Glide.with(this.context).load(countryInfoArrayList.get(position).getUserphoto()).into(placesViewHolder.userImg);
            if(countryInfoArrayList.get(position).isHasimg()) {
                placesViewHolder.gift.setVisibility(View.VISIBLE);
                placesViewHolder.txtcount.setVisibility(View.VISIBLE);
                placesViewHolder.txtcount.setText("");
                switch (countryInfoArrayList.get(position).getImgid()){

                    case "hearts":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_heart);
                        //   Constants.user.setDimond(Constants.user.getDimond()-500);
                        break;
                    case "like1":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_like_1);

                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "smilereact":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_heart_1_);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "pigions":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_pigeon);
                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "oscar":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_oscar);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "heartcomment":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_heartcomment);

                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;

                    case "like2":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_like);
                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "star":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_star);
                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "medal":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_medal);
                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "fire":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_fire);
                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "debate":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_debate);
                        // talntcoins.setText(Constants.user.getDimond()+"");
                        break;

                    case "castle":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_sand_castle);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "crown":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_crown);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;

                    case "carousel":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_carousel);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "championbelt":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_champion_belt);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;
                    case "clap":
                        placesViewHolder.gift.setImageResource(R.drawable.ic_clapping);
                        //  talntcoins.setText(Constants.user.getDimond()+"");
                        break;


                }
            }
            else {

                placesViewHolder.gift.setVisibility(View.GONE);
                placesViewHolder.txtcount.setVisibility(View.GONE);
            }







//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                //  Toast.makeText(context, "" + famousPlacesArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
//                showInterstitial(position);
//
//
    }

    @Override
    public int getItemCount() {
        return countryInfoArrayList.size();
    }

    public static class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        TextView txtcmnt;
        TextView txtuser,txtcount;
        CircleImageView userImg;
        GifImageView gift;

        ItemClickListener1 itemClickListener1;
        public PlacesViewHolder(View itemView, ItemClickListener1 listener1) {
            super(itemView);
            view = itemView;
            itemClickListener1=listener1;

            txtcount = view.findViewById(R.id.txtcount);
            gift = view.findViewById(R.id.imggift);
            userImg=view.findViewById(R.id.userimgcomment);
            txtcmnt = view.findViewById(R.id.cmnt);
            txtuser = view.findViewById(R.id.user);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClickListener1.onPositionClicked(view,getAdapterPosition());
        }
    }



}
