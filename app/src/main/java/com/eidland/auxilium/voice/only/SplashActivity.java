package com.eidland.auxilium.voice.only;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.agora.rtc.Constants;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Staticconfig;
import com.eidland.auxilium.voice.only.ui.LiveRoomActivity;

public class SplashActivity extends AppCompatActivity {
    DatabaseReference myRef;
    Intent intent;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //textView=findViewById(R.id.eidland);
      /*  mInterstitialAd = new InterstitialAd(this);

      // set the ad unit ID
      mInterstitialAd.setAdUnitId("ca-app-pub-3902756767389775/6264215741");

      initAndLoadInterstitialAds();*/
        //   Spannable spannable = new SpannableString("EIDLAND");
        //  spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //   textView.setText(spannable);
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            myRef = FirebaseDatabase.getInstance().getReference();

                            myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        String value = getSharedPreferences("Auxilium", MODE_PRIVATE).getString("Signupcomplete", "no");
                                        if (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("coins") && dataSnapshot.hasChild("imageurl") && dataSnapshot.hasChild("email")) {
                                            Staticconfig.user = dataSnapshot.getValue(User.class);
                                            //  Toast.makeText(SplashActivity.this, ""+StaticConfig.user.getCoin(), Toast.LENGTH_SHORT).show();

                                            intent = new Intent(SplashActivity.this, LiveRoomActivity.class);
                                            intent.putExtra("User", "Participent");
                                            intent.putExtra("userid", "A3qP5qyS34aGkFxQa3caaXxmHGl2");
                                            intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, "760232943A3qP5qyS34aGkFxQa3caaXxmHGl2");

                                            intent.putExtra("UserName", "Eidland Welcome Hall");
                                            intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");

                                            intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);

                                            startActivity(intent);
                                            finish();


                                        } else
                                            startActivity(new Intent(SplashActivity.this, Sign_Up_Activity.class));
                                        finish();
                                    } else
                                        startActivity(new Intent(SplashActivity.this, Sign_Up_Activity.class));
                                    finish();

                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } else {


                            startActivity(new Intent(SplashActivity.this, Sign_Up_Activity.class));
                            finish();


                        }
                    }
                }, 1000);
    }


}