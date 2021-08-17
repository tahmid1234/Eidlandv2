package com.eidland.auxilium.voice.only.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.DisplayMetrics;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.eidland.auxilium.voice.only.adapter.AdapterUpcomingSession;
import com.eidland.auxilium.voice.only.adapter.AdapterSeat;
import com.eidland.auxilium.voice.only.model.Comment;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.adapter.AdapterRoom;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.model.UpcomingSession;
import com.eidland.auxilium.voice.only.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.google.android.gms.tagmanager.InstallReferrerReceiver;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView UserName;
    ImageView UserPhoto, homeImage;
    String userid, username, imageurl;
    RecyclerView roomRecycler, upcomingSessionRV;
    ProgressBar progressbar;
    List<Rooms> roomsList = new ArrayList<Rooms>();
    List<UpcomingSession> upcomingSessionList = new ArrayList<>();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referrals");

    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    private final String prefKey = "checkedInstallReferrer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        progressbar = findViewById(R.id.progressbar);

        UserName = findViewById(R.id.username);
        UserPhoto = findViewById(R.id.userimage);
        homeImage = findViewById(R.id.home_image);

        roomRecycler = findViewById(R.id.rvrooms);
//        upcomingSessionRV = findViewById(R.id.upcomingSessionsRV);

        FirebaseDatabase.getInstance().getReference("HomeImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String url = snapshot.getValue().toString();
                    Glide.with(MainActivity.this).load(url).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(homeImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("AllRooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomsList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot child : snapshot.getChildren()) {
                        roomsList.add(child.getValue(Rooms.class));
                    }
                    GridLayoutManager seatLayoutManager = new GridLayoutManager(MainActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
                    AdapterRoom adapterRoom = new AdapterRoom(roomsList, MainActivity.this, width);
                    roomRecycler.setLayoutManager(seatLayoutManager);
                    adapterRoom.notifyDataSetChanged();
                    roomRecycler.setAdapter(adapterRoom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        FirebaseDatabase.getInstance().getReference("UpcomingSessions").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                upcomingSessionList.clear();
//                if(snapshot.exists()){
//                    for (DataSnapshot child : snapshot.getChildren()) {
//                        upcomingSessionList.add(child.getValue(UpcomingSession.class));
//                    }
//                    GridLayoutManager seatLayoutManager = new GridLayoutManager(MainActivity.this, 1, GridLayoutManager.VERTICAL, false);
//                    AdapterUpcomingSession adapterUpcomingSession = new AdapterUpcomingSession(MainActivity.this, upcomingSessionList);
//                    upcomingSessionRV.setLayoutManager(seatLayoutManager);
//                    adapterUpcomingSession.notifyDataSetChanged();
//                    upcomingSessionRV.setAdapter(adapterUpcomingSession);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

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

        try {
            checkInstallReferrer();
        } catch (Exception e) {
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


    void checkInstallReferrer() {
//      remove the if condition to check referrer, need to check more on the prefKey usage
        if (getPreferences(MODE_PRIVATE).getBoolean(prefKey, false)) {
            Toast.makeText(getApplicationContext(), StaticConfig.user.getReferrer(), Toast.LENGTH_SHORT);
            if (StaticConfig.user.getReferrer()== null || StaticConfig.user.getReferrer()== "utm_source=google-play&utm_medium=organic"){
                Toast.makeText(getApplicationContext(), "not eligible", Toast.LENGTH_SHORT);
            }
            return;
        }

        InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(this).build();
        backgroundExecutor.execute(() -> getInstallReferrerFromClient(referrerClient));
    }

    void getInstallReferrerFromClient(InstallReferrerClient referrerClient) {

        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            return;
                        }
                        final String referrerUrl = response.getInstallReferrer();


                        // TODO: If you're using GTM, call trackInstallReferrerforGTM instead.
                        trackInstallReferrer(referrerUrl);

                        // Only check this once.
                        getPreferences(MODE_PRIVATE).edit().putBoolean(prefKey, true).commit();

                        //reward logic
                        Long currentBalance = Long.parseLong(StaticConfig.user.getCoins());
                        currentBalance += 50;
                        StaticConfig.user.setCoins(currentBalance.toString());
                        userRef.child(currentUser.getUid()).child("coins").setValue(currentBalance.toString());
                        Toast.makeText(getApplicationContext(), "Congratulations!! you received 50 coins", Toast.LENGTH_LONG).show();
                        referralRef.child(referrerUrl).child(StaticConfig.user.getReferralURL()).setValue("referred");
                        userRef.child(currentUser.getUid()).child("referrer").setValue(referrerUrl);

                        // End the connection
                        referrerClient.endConnection();

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }

    // Tracker for Classic GA (call this if you are using Classic GA only)
    private void trackInstallReferrer(final String referrerUrl) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                CampaignTrackingReceiver receiver = new CampaignTrackingReceiver();
                Intent intent = new Intent("com.android.vending.INSTALL_REFERRER");
                intent.putExtra("referrer", referrerUrl);
                receiver.onReceive(getApplicationContext(), intent);

            }
        });
    }
}
