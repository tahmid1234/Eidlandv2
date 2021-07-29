package com.eidland.auxilium.voice.only.adapter;

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
import com.eidland.auxilium.voice.only.helper.ConstantApp;

public class AdapterGame extends RecyclerView.Adapter<AdapterGame.ViewHolder> {

    private Context context;
    private OnGameClickListener onGameClickListener;
    private int width;

    public AdapterGame(Context context, OnGameClickListener onGameClickListener, int width) {
        this.context = context;
        this.onGameClickListener = onGameClickListener;
        this.width = width;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_game, parent, false), onGameClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.gameIcon.setMaxWidth(width);
        holder.gameIcon.setMinimumWidth(width);
        Glide.with(context).load(ConstantApp.gameList().get(position).deckImage).into(holder.gameIcon);
        holder.gameName.setText(ConstantApp.gameList().get(position).deckName);

    }

    @Override
    public int getItemCount() {
        return ConstantApp.gameList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView gameIcon;
        LinearLayout gameLayout;
        TextView gameName;

        OnGameClickListener onGameClickListener;

        public ViewHolder(@NonNull View itemView, OnGameClickListener onGameClickListener) {
            super(itemView);
            gameIcon = itemView.findViewById(R.id.game_icon);
            gameLayout = itemView.findViewById(R.id.game_layout);
            gameName = itemView.findViewById(R.id.game_name);
            this.onGameClickListener = onGameClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onGameClickListener.onGameClick(getAdapterPosition(), gameIcon);
        }
    }

    public interface OnGameClickListener {
        void onGameClick(int position, ImageView gameIcon);
    }
}
