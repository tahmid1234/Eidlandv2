package com.eidland.auxilium.voice.only.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import io.agora.rtc.Constants;

import com.eidland.auxilium.voice.only.BuildConfig;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

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
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                            User user = new User("user", "user@gmail.com", "https://auxiliumlivestreaming.000webhostapp.com/avatar/1.png", "0", "0", "null", "null");
                            StaticConfig.user = user;
                            myRef = FirebaseDatabase.getInstance().getReference();
                            Intent appLinkIntent = getIntent();
                            String appLinkAction = appLinkIntent.getAction();
                            Uri appLinkData = appLinkIntent.getData();
                            myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        if (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("coins") && dataSnapshot.hasChild("imageurl") && dataSnapshot.hasChild("email")) {
                                            Toast.makeText(getApplicationContext(), appLinkIntent.toString(), Toast.LENGTH_SHORT);
                                            try {
                                                if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
                                                    StaticConfig.user = dataSnapshot.getValue(User.class);
                                                    intent = new Intent(SplashActivity.this, LiveRoomActivity.class);
                                                    Toast.makeText(getApplicationContext(), "action: " + appLinkAction, Toast.LENGTH_SHORT);
                                                    Toast.makeText(getApplicationContext(), "data: " + appLinkData.toString(), Toast.LENGTH_SHORT);
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
                    }
                }, 1000);
    }
}