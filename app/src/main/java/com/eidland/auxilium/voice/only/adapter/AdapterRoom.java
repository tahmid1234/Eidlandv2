package com.eidland.auxilium.voice.only.adapter;

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

public class AdapterRoom extends FirebaseRecyclerAdapter<Rooms, AdapterRoom.myviewholder> {
    Rooms room;

    public AdapterRoom(@NonNull FirebaseRecyclerOptions<Rooms> options) {
        super(options);
        room = new Rooms();
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final Rooms model) {
        holder.tvname.setText(model.getName());
        holder.tvdes.setText("1.1K Members, ");
        holder.tvdes.append(model.getViewers() + " online");
        //   holder.tvdes.setText(model.getViewers()+" Members");
        Glide.with(holder.ivphoto.getContext()).load(model.getImageurl()).into(holder.ivphoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.ivphoto.getContext(), LiveRoomActivity.class);
                intent.putExtra("User", "Participent");
                intent.putExtra("userid", model.getHostuid());
                intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, model.getRoomname());

                intent.putExtra("UserName", model.getName());
                intent.putExtra("profile", model.getImageurl());
                intent.putExtra("token", model.getToken());
                intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
             /*   Intent intent = new Intent(holder.ivphoto.getContext(), LiveRoomActivity.class);
                intent.putExtra("User","Participent");
                intent.putExtra("userid", model.getHostuid());
                intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, model.getRoomname());

                intent.putExtra("UserName", model.getName());
                intent.putExtra("profile", model.getImageurl());
                intent.putExtra("token", model.getToken());
                intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);*/
                holder.ivphoto.getContext().startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleroomxml, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView tvname;
        TextView tvdes;
        ImageView ivphoto;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id._tvname);
            ivphoto = itemView.findViewById(R.id._ivphoto);
            tvdes = itemView.findViewById(R.id._tvdes);


        }
    }
}
