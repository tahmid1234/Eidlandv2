package com.eidland.auxilium.voice.only.adapter;

import static com.eidland.auxilium.voice.only.helper.DateFormater.getHour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.activity.LiveRoomActivity;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

import io.agora.rtc.Constants;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.ViewHolder> {
    List<Rooms> rooms;
    Context context;
    int width=0;

    public AdapterRoom(List<Rooms> rooms, Context context, int width) {
        this.rooms = rooms;
        this.context = context;
        this.width = width;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Rooms room = rooms.get(position);

        String now = String.valueOf(Calendar.getInstance().getTimeInMillis());

        holder.roomName.setText(rooms.get(position).getName());
//        holder.roomCard.setMinimumWidth(width/2);
        FirebaseDatabase.getInstance().getReference("Viewers").child(room.roomname).addValueEventListener(new ValueEventListener() {
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

        Glide.with(holder.roomPhoto.getContext()).load(room.imageurl).into(holder.roomPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double current= getHour(now);
                double roomstart=   getHour(room.startTime);
                double roomend= getHour(room.endTime);
                if(getHour(room.startTime)<=getHour(now) && getHour(room.endTime)>=getHour(now)){
//                if(true){
                    Intent intent = new Intent(holder.roomPhoto.getContext(), LiveRoomActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("User", "Participent");
                    intent.putExtra("userid", room.hostuid);
                    intent.putExtra("welcomemsg", room.getWelcomemsg());
                    intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room.roomname);
                    intent.putExtra("UserName", room.name);
                    intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");
                    intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                    context.startActivity(intent);
                }else{
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_custom_dialog);
                    LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
                    linearLayout.setMinimumWidth((int) (width* 0.8));
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
                    dialog.setCancelable(false);

                    ImageView imageView = dialog.findViewById(R.id.dialog_icon);
                    imageView.setImageResource(R.drawable.ic_exit);
                    imageView.setVisibility(View.VISIBLE);

                    TextView msg = dialog.findViewById(R.id.msg);
                    msg.setText(room.offTimeMsg);

                    TextView negative = dialog.findViewById(R.id.positive_btn);
                    RelativeLayout areatop=dialog.findViewById(R.id.topdialogbutton);
                    negative.setVisibility(View.VISIBLE);
                    negative.setText("OKAY");
                    areatop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
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
        CardView roomCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id._tvname);
            roomPhoto = itemView.findViewById(R.id._ivphoto);
            memberNumber = itemView.findViewById(R.id._tvdes);
            roomCard = itemView.findViewById(R.id.room_card);
        }
    }
}
