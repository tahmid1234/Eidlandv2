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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import io.agora.rtc.Constants;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.ViewHolder> {
    List<Rooms> rooms;
    Context context;

    public AdapterRoom(List<Rooms> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        holder.roomName.setText(rooms.get(position).getName());

        FirebaseDatabase.getInstance().getReference("Viewers").child(rooms.get(position).roomname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.memberNumber.setText(snapshot.getChildrenCount()+" Online");
                }else {
                    holder.memberNumber.setText("0 Online");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Glide.with(holder.roomPhoto.getContext()).load(rooms.get(position).getImageurl()).into(holder.roomPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.roomPhoto.getContext(), LiveRoomActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public int getItemCount() {
        return rooms.size();
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
