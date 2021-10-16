package com.eidland.auxilium.voice.only.activity;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eidland.auxilium.voice.only.BuildConfig;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.agora.rtc.Constants;

import static android.content.ContentValues.TAG;
import static com.eidland.auxilium.voice.only.helper.DateFormater.getHour;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference myRef;
    Intent intent;
    private static final String FB_RC_KEY_TITLE = "update_title";
    private static final String FB_RC_KEY_DESCRIPTION = "update_description";
    private static final String FB_RC_KEY_FORCE_UPDATE_VERSION = "force_update_version";
    private static final String FB_RC_KEY_LATEST_VERSION = "latest_version";
    AppUpdateDialog appUpdateDialog;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onStart() {
        super.onStart();
        checkAppUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "FCM";
            String channelName = "FCM";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        Log.d(TAG, token);
                    }
                });

        CheckNextActivity();
    }

    public void checkAppUpdate() {

        final int versionCode = BuildConfig.VERSION_CODE;
        final HashMap<String, Object> defaultMap = new HashMap<>();
        defaultMap.put(FB_RC_KEY_TITLE, "Update Available");
        defaultMap.put(FB_RC_KEY_DESCRIPTION, "A new version of the application is available please click below to update the latest version.");
        defaultMap.put(FB_RC_KEY_FORCE_UPDATE_VERSION, "" + versionCode);
        defaultMap.put(FB_RC_KEY_LATEST_VERSION, "" + versionCode);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());
        mFirebaseRemoteConfig.setDefaultsAsync(defaultMap);
        Task<Void> fetchTask = mFirebaseRemoteConfig.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toSeconds(4));

        fetchTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    mFirebaseRemoteConfig.fetchAndActivate();
                    String title = getValue(FB_RC_KEY_TITLE, defaultMap);
                    String description = getValue(FB_RC_KEY_DESCRIPTION, defaultMap);
                    int forceUpdateVersion = Integer.parseInt(getValue(FB_RC_KEY_FORCE_UPDATE_VERSION, defaultMap));
                    int latestAppVersion = Integer.parseInt(getValue(FB_RC_KEY_LATEST_VERSION, defaultMap));
                    boolean isCancelable = true;
                    if (latestAppVersion > versionCode) {
                        if (forceUpdateVersion > versionCode)
                            isCancelable = false;

                        appUpdateDialog = new AppUpdateDialog(SplashActivity.this, title, description, isCancelable);
                        appUpdateDialog.setCancelable(false);
                        appUpdateDialog.show();

                        Window window = appUpdateDialog.getWindow();
                        assert window != null;
                        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

                    }
                } else {
                    Toast.makeText(SplashActivity.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getValue(String parameterKey, HashMap<String, Object> defaultMap) {
        String value = mFirebaseRemoteConfig.getString(parameterKey);
        if (TextUtils.isEmpty(value))
            value = (String) defaultMap.get(parameterKey);
        return value;
    }

    public void CheckNextActivity() {
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                                User user = new User("user", "user@gmail.com", "https://auxiliumlivestreaming.000webhostapp.com/avatar/1.png", "0", "0", "null", "null");
                                StaticConfig.user = user;
                                myRef = FirebaseDatabase.getInstance().getReference();
                                Intent appLinkIntent = getIntent();
                                String appLinkAction = appLinkIntent.getAction();
                                Uri appLinkData = appLinkIntent.getData();
//                            List<String> pathSegments = appLinkData.getPathSegments();
                                myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            if (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("coins") && dataSnapshot.hasChild("imageurl") && dataSnapshot.hasChild("email")) {
                                                try {
                                                    if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
                                                        FirebaseDatabase.getInstance().getReference().child("AllRooms").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                                                        Rooms room = dataSnapshot1.getValue(Rooms.class);
                                                                        if (appLinkData.toString().contains(room.getRoomname())) {
                                                                            String now = String.valueOf(Calendar.getInstance().getTimeInMillis());
                                                                            if (getHour(room.startTime) <= getHour(now) && getHour(room.endTime) >= getHour(now)) {
                                                                                Intent intent = new Intent(getApplicationContext(), LiveRoomActivity.class);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                intent.putExtra("User", "Participent");
                                                                                intent.putExtra("userid", room.hostuid);
                                                                                intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, room.roomname);
                                                                                intent.putExtra("UserName", room.name);
                                                                                intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");
                                                                                intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                                                                                startActivity(intent);
                                                                            } else {
                                                                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                                                                int width = displayMetrics.widthPixels;
                                                                                Dialog dialog = new Dialog(getApplicationContext());
                                                                                dialog.setContentView(R.layout.layout_custom_dialog);
                                                                                LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
                                                                                linearLayout.setMinimumWidth((int) (width * 0.8));
                                                                     //           dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
                                                                                dialog.setCancelable(false);
                                                                                ImageView imageView = dialog.findViewById(R.id.dialog_icon);
                                                                                imageView.setVisibility(View.VISIBLE);

                                                                                TextView msg = dialog.findViewById(R.id.msg);
                                                                                msg.setText(room.offTimeMsg);

                                                                                TextView negative = dialog.findViewById(R.id.positive_btn);
                                                                                negative.setVisibility(View.VISIBLE);
                                                                                negative.setText("OKAY");
                                                                                negative.setOnClickListener(new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View view) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                });
                                                                                dialog.show();

                                                                            }
                                                                        }

                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println(e);
                                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                                                }
                                                StaticConfig.user = dataSnapshot.getValue(User.class);
                                                intent = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else
                                                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                                            finish();
                                        } else {
                                            startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
                                finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 1000);
    }
}