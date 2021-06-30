package com.eidland.auxilium.voice.only.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Viewer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdapterGift extends RecyclerView.Adapter<AdapterGift.ViewHolder> {

    private Context context;
    private OnGiftClickListener onGiftClickListener;
    private int width;

    public AdapterGift(Context context, OnGiftClickListener onGiftClickListener, int width) {
        this.context = context;
        this.onGiftClickListener = onGiftClickListener;
        this.width = width;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_gift, parent, false), onGiftClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.giftLayout.setMinimumWidth(width);
        holder.giftIcon.setBackgroundResource(ConstantApp.giftList().get(position).image);
    }

    @Override
    public int getItemCount() {
        return ConstantApp.giftList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView giftIcon;
        LinearLayout giftLayout;

        OnGiftClickListener onGiftClickListener;

        public ViewHolder(@NonNull View itemView, OnGiftClickListener onGiftClickListener) {
            super(itemView);
            giftIcon = itemView.findViewById(R.id.gift_icon);
            giftLayout = itemView.findViewById(R.id.gift_layout);
            this.onGiftClickListener = onGiftClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGiftClickListener.onGiftClick(getAdapterPosition(), giftIcon);
        }
    }

    public interface OnGiftClickListener {
        void onGiftClick(int position, ImageView giftIcon);
    }
}
