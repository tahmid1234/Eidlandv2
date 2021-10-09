package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.Leader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterLeadUser extends RecyclerView.Adapter<AdapterLeadUser.ViewHolder> {
    private Context context;
    private List<Leader> leaders;

    public AdapterLeadUser(Context context, List<Leader> leaders) {
        this.context = context;
        this.leaders = leaders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_lead_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.points.setText(Long.toString(leaders.get(position).coins));
        Glide.with(context).load(leaders.get(position).imgUrl).placeholder(R.drawable.ic_mic).into(holder.userIcon);
    }

    @Override
    public int getItemCount() {
        return Math.min(leaders.size(), 3);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userIcon;
        TextView points;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = itemView.findViewById(R.id.user_icon);
            points = itemView.findViewById(R.id.points);
        }
    }
}
