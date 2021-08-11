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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.eidland.auxilium.voice.only.adapter.AdapterSeat;
import com.eidland.auxilium.voice.only.model.Comment;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.adapter.AdapterRoom;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral");

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

        try{
            referralcheck();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
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

    public void referralcheck(){
        InstallReferrerClient mReferrerClient;
        mReferrerClient = InstallReferrerClient.newBuilder(this).build();
        mReferrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try {
                            ReferrerDetails response = mReferrerClient.getInstallReferrer();
                            if (!response.getInstallReferrer().contains("utm_source"))
                            {
                                Toast.makeText(getApplicationContext(), response.getInstallReferrer(), Toast.LENGTH_LONG).show();
                                userRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);
                                        user.setCoins(user.getCoins()+50);
                                        userRef.child(currentUser.getUid()).child("Coins").setValue(user.getCoins());
                                        Toast.makeText(getApplicationContext(), "Congratulations!! you received 50 coins", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mReferrerClient.endConnection();
                        break;
                    case
                            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        Toast.makeText(getApplicationContext(), "Feature Not Supported", Toast.LENGTH_LONG).show();
                        break;
                    case
                            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), "Service Unavailable", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

    }
}
