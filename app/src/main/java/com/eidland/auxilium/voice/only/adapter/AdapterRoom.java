package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.eidland.auxilium.voice.only.R;

import com.eidland.auxilium.voice.only.activity.LiveRoomActivity;

import io.agora.rtc.Constants;

public class AdapterRoom extends FirebaseRecyclerAdapter<Rooms, AdapterRoom.ViewHolder> {
    Rooms room;
    Context context;

    public AdapterRoom(@NonNull FirebaseRecyclerOptions<Rooms> options, Context context) {
        super(options);
        room = new Rooms();
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Rooms model) {
        holder.roomName.setText(model.getName());
        holder.memberNumber.setText("1.1K Members, ");
        holder.memberNumber.append(model.getViewers() + " online");
        //   holder.tvdes.setText(model.getViewers()+" Members");


        Glide.with(holder.roomPhoto.getContext()).load(model.getImageurl()).into(holder.roomPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.roomPhoto.getContext(), LiveRoomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("User", "Participent");
                intent.putExtra("userid", "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
                intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, "760232943A3qP5qyS34aGkFxQa3caaXxmHGl2");
                intent.putExtra("UserName", "Eidland Battle Royale");
                intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");
                intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                context.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleroomxml, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;
        TextView memberNumber;
        ImageView roomPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id._tvname);
            roomPhoto = itemView.findViewById(R.id._ivphoto);
            memberNumber = itemView.findViewById(R.id._tvdes);


        }
    }
}
