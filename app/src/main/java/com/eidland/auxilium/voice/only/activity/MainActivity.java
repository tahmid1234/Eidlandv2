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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.adapter.AdapterRoom;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.eidland.auxilium.voice.only.MyProfileActivity;
import com.eidland.auxilium.voice.only.R;

public class MainActivity extends BaseActivity {
    ProgressDialog progressDialog;
    TextView UserName;
    ImageView UserPhoto;
    //    ImageView button_join;
    String userid, username, imageurl;
    RecyclerView roomRecycler;
    AdapterRoom roomAdapter;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressbar = findViewById(R.id.progressbar);

        UserName = findViewById(R.id.username);
        UserPhoto = findViewById(R.id.userimage);

        roomRecycler = findViewById(R.id.rvrooms);

        roomRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        FirebaseRecyclerOptions<Rooms> options
                = new FirebaseRecyclerOptions.Builder<Rooms>()
                .setQuery(FirebaseDatabase.getInstance().getReference("AllRooms"), Rooms.class)
                .build();
        roomAdapter = new AdapterRoom(options, MainActivity.this);
        roomRecycler.setAdapter(roomAdapter);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...!");
        progressDialog.setMessage("Processing Data...!");
        progressDialog.setCancelable(false);
        progressbar.setVisibility(View.GONE);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        username = StaticConfig.user.getName();
        imageurl = StaticConfig.user.getImageurl();
        UserName.setText(username);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!MainActivity.this.isDestroyed())
                Glide.with(MainActivity.this).load(imageurl).into(UserPhoto);
        } else {

            Glide.with(MainActivity.this).load(imageurl).into(UserPhoto);
        }
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
        roomAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        roomAdapter.stopListening();
    }

    public void onClickJoin(View view) {
        roomcheck();

    }

    public void myProfile(View view) {
        Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
        startActivity(intent);

    }

    public void roomcheck() {
        startActivity(new Intent(MainActivity.this, EnterRoomActivity.class));
    }

}
