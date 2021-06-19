package com.eidland.auxilium.voice.only.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.Viewer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdapterSeat extends RecyclerView.Adapter<AdapterSeat.ViewHolder> {

    private Context context;
    private OnSeatClickListener onSeatClickListener;
    private String roomName;

    public AdapterSeat(Context context, OnSeatClickListener onSeatClickListener, String roomName) {
        this.context = context;
        this.onSeatClickListener = onSeatClickListener;
        this.roomName = roomName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_seat, parent, false), onSeatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child("seat"+position).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try{
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        holder.seatName.setText(viewer.name);
                        Glide.with(context).load(viewer.getPhotoUrl()).placeholder(R.drawable.ic_mic).into(holder.seatImage);
                    }catch (Exception e){
                        System.out.println(e);
                        holder.seatName.setText("Seat #" + (position + 1));
                        holder.seatImage.setImageResource(R.drawable.ic_mic);
                    }
                }else {
                    holder.seatName.setText("Seat #" + (position + 1));
                    holder.seatImage.setImageResource(R.drawable.ic_mic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child("seat"+position).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if (snapshot.exists()) {
//                    try{
//                        Viewer viewer = snapshot.getValue(Viewer.class);
//                        holder.seatName.setText(viewer.name);
//                        Glide.with(context).load(viewer.getPhotoUrl()).placeholder(R.drawable.ic_mic).into(holder.seatImage);
//                    }catch (Exception e){
//                        System.out.println(e);
//                        holder.seatName.setText("Seat #" + (position + 1));
//                        holder.seatImage.setImageResource(R.drawable.ic_mic);
//                    }
//                }else {
//                    holder.seatName.setText("Seat #" + (position + 1));
//                    holder.seatImage.setImageResource(R.drawable.ic_mic);
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue() != null) {
//                    holder.seatName.setText("Seat #" + (position + 1));
//                    holder.seatImage.setImageResource(R.drawable.ic_mic);
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView seatName;
        ImageView seatImage;
        LinearLayout seat;

        OnSeatClickListener onSeatClickListener;

        public ViewHolder(@NonNull View itemView, OnSeatClickListener onSeatClickListener) {
            super(itemView);
            seatName = itemView.findViewById(R.id.host_name);
            seatImage = itemView.findViewById(R.id.host_image);
            seat = itemView.findViewById(R.id.seat);

            this.onSeatClickListener = onSeatClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSeatClickListener.onSeatClick(getAdapterPosition());
        }
    }

    public interface OnSeatClickListener {
        void onSeatClick(int position);
    }
}
