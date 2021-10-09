package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.DateFormater;
import com.eidland.auxilium.voice.only.model.UpcomingSession;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterUpcomingSession extends RecyclerView.Adapter<AdapterUpcomingSession.ViewHolder> {
    private Context context;
    List<UpcomingSession> upcomingSessions;

    public AdapterUpcomingSession(Context context, List<UpcomingSession> upcomingSessions) {
        this.context = context;
        this.upcomingSessions = upcomingSessions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_upcoming_session, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UpcomingSession upcomingSession = upcomingSessions.get(position);
        holder.sessionTitle.setText(upcomingSession.title);
        holder.sessionDesc.setText(upcomingSession.description);
        holder.sessionTime.setText(DateFormater.getSessionDate(upcomingSession.startTime));
        Glide.with(context).load(upcomingSession.iconUrl).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(holder.sessionImage);
    }

    @Override
    public int getItemCount() {
        return upcomingSessions.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionTitle;
        TextView sessionDesc;
        ImageView sessionImage;
        TextView sessionTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTitle = itemView.findViewById(R.id.sessionTitle);
            sessionDesc = itemView.findViewById(R.id.sessionDescription);
            sessionTime = itemView.findViewById(R.id.startTime);
            sessionImage = itemView.findViewById(R.id.upcoming_session_icon);

        }

    }

}
