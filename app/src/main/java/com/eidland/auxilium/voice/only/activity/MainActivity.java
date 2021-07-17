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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.adapter.AdapterRoom;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.eidland.auxilium.voice.only.MyProfileActivity;
import com.eidland.auxilium.voice.only.R;

public class MainActivity extends BaseActivity {
    String Seats, UserName;
    ProgressDialog progressDialog;
    TextView tvuname;
    ImageView ivuphoto, button_join;
    String userid, username, imageurl, description;
    RecyclerView rvrooms;
    AdapterRoom myadapter;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = findViewById(R.id.progressbar);
        tvuname = findViewById(R.id.username);
        ivuphoto = findViewById(R.id.userimage);
        button_join = findViewById(R.id.button_join);
        rvrooms = findViewById(R.id.rvrooms);
        rvrooms.setLayoutManager(new GridLayoutManager(this, 2));
        FirebaseRecyclerOptions<Rooms> options
                = new FirebaseRecyclerOptions.Builder<Rooms>()
                .setQuery(FirebaseDatabase.getInstance().getReference("AllRooms"), Rooms.class)
                .build();
        myadapter = new AdapterRoom(options);
        rvrooms.setAdapter(myadapter);
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...!");
        progressDialog.setMessage("Processing Data...!");
        progressDialog.setCancelable(false);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    button_join.setVisibility(View.VISIBLE);
                } else {
                    button_join.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        username = StaticConfig.user.getName();
        imageurl = StaticConfig.user.getImageurl();
        tvuname.setText(username);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!MainActivity.this.isDestroyed())
                Glide.with(MainActivity.this).load(imageurl).into(ivuphoto);
        } else {

            Glide.with(MainActivity.this).load(imageurl).into(ivuphoto);
        }
        progressbar.setVisibility(View.GONE);


    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myadapter.stopListening();
    }

    public void onClickJoin(View view) {
        // show dialog to choose role
        roomcheck();

    }

    public void myProfile(View view) {
        Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
        startActivity(intent);

    }

    public void roomcheck() {

        startActivity(new Intent(MainActivity.this, EnterRoomActivity.class));

//

    }

}
