package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.ConstantApp;

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
        holder.giftIcon.setMaxWidth(width);
        holder.giftIcon.setMinimumWidth(width);
        holder.giftIcon.setBackgroundResource(ConstantApp.giftList().get(position).image);
        if (!ConstantApp.giftList().get(position).tag.equals(""))

        {   holder.gifttagholder.setVisibility(View.VISIBLE);
            holder.giftIdentifier.setVisibility(View.VISIBLE);
            holder.giftIdentifier.setText(ConstantApp.giftList().get(position).tag);
        }
        else {
            holder.gifttagholder.setVisibility(View.INVISIBLE);
            holder.giftIdentifier.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ConstantApp.giftList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        ImageView giftIcon;
        RelativeLayout selection;
        RelativeLayout giftLayout;
        TextView giftIdentifier;
        RelativeLayout gifttagholder;
        OnGiftClickListener onGiftClickListener;

        public ViewHolder(@NonNull View itemView, OnGiftClickListener onGiftClickListener) {
            super(itemView);
            giftIcon = itemView.findViewById(R.id.gift_icon);
            selection=itemView.findViewById(R.id.gift_check_1);
            giftIdentifier=itemView.findViewById(R.id.gift_identifier);
            gifttagholder=itemView.findViewById(R.id.gift_identifier_holder);
            giftLayout = itemView.findViewById(R.id.gift_layout);
            this.onGiftClickListener = onGiftClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGiftClickListener.onGiftClick(getAdapterPosition(), giftIcon,selection);
        }
    }

    public interface OnGiftClickListener {
        void onGiftClick(int position, ImageView giftIcon,RelativeLayout selection);
    }
}
