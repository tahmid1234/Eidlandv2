package com.eidland.auxilium.voice.only.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.adapter.AdapterSeat;
import com.eidland.auxilium.voice.only.model.Comment;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.adapter.AdapterRoom;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.eidland.auxilium.voice.only.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView UserName;
    ImageView UserPhoto;
    String userid, username, imageurl;
    RecyclerView roomRecycler;
    ProgressBar progressbar;
    List<Rooms> roomsList = new ArrayList<Rooms>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressbar = findViewById(R.id.progressbar);

        UserName = findViewById(R.id.username);
        UserPhoto = findViewById(R.id.userimage);

        roomRecycler = findViewById(R.id.rvrooms);

        FirebaseDatabase.getInstance().getReference("AllRooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot child : snapshot.getChildren()) {
                        roomsList.add(child.getValue(Rooms.class));
                    }
                    GridLayoutManager seatLayoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
                    AdapterRoom adapterRoom = new AdapterRoom(roomsList, MainActivity.this);
                    roomRecycler.setLayoutManager(seatLayoutManager);
                    adapterRoom.notifyDataSetChanged();
                    roomRecycler.setAdapter(adapterRoom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...!");
        progressDialog.setMessage("Processing Data...!");
        progressDialog.setCancelable(false);
        progressbar.setVisibility(View.GONE);

//        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        username = StaticConfig.user.getName();
//        imageurl = StaticConfig.user.getImageurl();
//        UserName.setText(username);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            if (!MainActivity.this.isDestroyed())
//                Glide.with(MainActivity.this).load(imageurl).into(UserPhoto);
//        } else {
//
//            Glide.with(MainActivity.this).load(imageurl).into(UserPhoto);
//        }
    }

    public void onClickJoin(View view) {
//        roomcheck();

    }

    public void myProfile(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);

    }

    public void roomcheck() {
        startActivity(new Intent(MainActivity.this, EnterRoomActivity.class));
    }

}
