package com.eidland.auxilium.voice.only.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.UidPositions;
import com.eidland.auxilium.voice.only.model.Viewer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSeat extends RecyclerView.Adapter<AdapterSeat.ViewHolder> {

    private Context context;
    private OnSeatClickListener onSeatClickListener;
    private String roomName;

    Integer[] seats = {
            R.drawable.ic_turquoise,
            R.drawable.ic_red,
            R.drawable.ic_skyblue,
            R.drawable.ic_yellow,
            R.drawable.ic_orange,
            R.drawable.ic_purple,
            R.drawable.ic_blue,
            R.drawable.ic_green
    };
    ArrayList<UidPositions> uidPositions;

    public AdapterSeat(Context context, OnSeatClickListener onSeatClickListener, String roomName) {
        this.context = context;
        this.onSeatClickListener = onSeatClickListener;
        this.roomName = roomName;
        uidPositions = new ArrayList<>(8);
        initVacancies();
       // seats = context.getResources().getIntArray(R.array.seats);
    }

    private void initVacancies() {

        for (int i = 0; i < 8; i++) {

            uidPositions.add(new UidPositions(0, i));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_seat, parent, false), onSeatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        uidPositions.get(position).initAnimator(context,
                holder.itemView.findViewById(R.id.cardback),
                holder.itemView.findViewById(R.id.cardback1));
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child("seat" + position).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        //assert viewer != null;
                        holder.seatName.setText(viewer.name);
                        holder.cardback.setVisibility(View.VISIBLE);
                        holder.cardback1.setVisibility(View.VISIBLE);
                        holder.micImage.setImageResource(0);
                        Glide.with(context.getApplicationContext()).load(viewer.getPhotoUrl()).placeholder(R.drawable.ic_mic_on).into(holder.seatImage);
                        uidPositions.get(position).setIsfill(true);
                        uidPositions.get(position).uid = viewer.uid;

                    } catch (Exception e) {
                        System.out.println(e);
                        holder.seatName.setText((position + 1));
                    //    holder.seatImage.setImageResource(R.drawable.ic_mic);
                        holder.micImage.setImageResource(R.drawable.ic_micn);
                        holder.seatImage.setImageResource(0);
                        uidPositions.get(position).setIsfill(false);
                        uidPositions.get(position).uid = 0;

                        holder.cardback.setVisibility(View.GONE);
                        holder.cardback1.setVisibility(View.GONE);
                    }
                } else {
                    holder.seatName.setText(String.format("%d", position + 1));
               //     holder.seatImage.setImageResource(R.drawable.ic_mic);
                    holder.micImage.setImageResource(R.drawable.ic_micn);
                    holder.seatImage.setImageResource(0);
                    holder.seatImage.setBackgroundResource(seats[position]);
                    uidPositions.get(position).setIsfill(false);
                    uidPositions.get(position).uid = 0;

                    holder.cardback.setVisibility(View.GONE);
                    holder.cardback1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public void indicateSpeaking(List<Integer> uidList) {

        for (int k = 0; k < uidPositions.size(); k++) {
            if (uidPositions.get(k).isfill)
                if (uidList.contains(uidPositions.get(k).uid)) {

                    uidPositions.get(k).startAnimation();
                }

        }
    }

    public void stopIndicateSpeaking() {
        for (int i = 0; i < 8; i++) {
            uidPositions.get(i).stopAnimation();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView seatName;
        ImageView seatImage;
        ImageView micImage;
        LinearLayout seat;
        CardView cardback1, cardback;
        OnSeatClickListener onSeatClickListener;

        public ViewHolder(@NonNull View itemView, OnSeatClickListener onSeatClickListener) {
            super(itemView);
            seatName = itemView.findViewById(R.id.host_name);
            seatImage = itemView.findViewById(R.id.host_image);
            micImage = itemView.findViewById(R.id.mic_image);
            cardback = itemView.findViewById(R.id.cardback);
            cardback1 = itemView.findViewById(R.id.cardback1);
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
