package com.eidland.auxilium.voice.only.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.Helper;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ImageView userimg;
    LinearLayout button_join, lrnrefrsh;
    String userid;
    TextView txtname, txtcoins, txtrcvcoins;
    DecimalFormat formatter;
    String finalText, coincomma;
    FirebaseAuth mAuth;
    Boolean edited = false;
    String PhotoUrl;
    TextView referralLinkText;
    FirebaseUser currentUser;
    String referralURL = "https://play.google.com/store/apps/details?id=com.eidland.auxilium.voice.only&referrer=";
    String referralCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile2);
        lrnrefrsh = findViewById(R.id.lrnrefresh);
        userimg = findViewById(R.id.userimg);
        txtname = findViewById(R.id.txtname);
        txtcoins = findViewById(R.id.txtcoins);
        txtrcvcoins = findViewById(R.id.txtrcvcoins);
        button_join = findViewById(R.id.lrnjoin);
        referralLinkText = findViewById(R.id.referrallinktext);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userid.equals("cJupIaBOKXN8QqWzAQMQYFwHzVC3")) {
            lrnrefrsh.setVisibility(View.VISIBLE);
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userid)) {
                    button_join.setVisibility(View.VISIBLE);
                } else {
                    button_join.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getReferralURL()=="null")
                {
                    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
                    StringBuilder sb = new StringBuilder();
                    sb.append(referralURL);
                    referralCode= " ";
                    for (int i = 0; i < 8; i++) {
                        int index = (int)(AlphaNumericString.length() * Math.random());
                        referralCode += AlphaNumericString.charAt(index);
                        sb.append(AlphaNumericString.charAt(index));
                    }
                    user.setReferralURL(referralCode);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("referralURL").setValue(user.getReferralURL());
                    FirebaseDatabase.getInstance().getReference().child("Referrals").child(user.getReferralURL()).setValue("init");
                    referralURL = sb.toString();
                    referralLinkText.setText(referralURL);
                }
                else{
                    referralURL = "https://play.google.com/store/apps/details?id=com.eidland.auxilium.voice.only&referrer=";
                    referralURL += user.getReferralURL();
                    referralLinkText.setText(referralURL);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        referralLinkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("url", referralURL);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gettoken(final String roomname) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(ProfileActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, "https://auxilium2.herokuapp.com/access_token?channel=" + roomname, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("token");
                    FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomname).child("token").setValue(token);
                    Toast.makeText(ProfileActivity.this, roomname + " token added", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Log.e("roor", e.getLocalizedMessage() + "d");
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volly error", error.getLocalizedMessage() + "vghg");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }

    public void finish(View view) {
        StaticConfig.user = new User(StaticConfig.user.getName(), StaticConfig.user.getEmail(), PhotoUrl, StaticConfig.user.getCoins(), StaticConfig.user.getReceivedCoins(), StaticConfig.user.getReferralURL());
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StaticConfig.user != null) {
            txtname.setText(StaticConfig.user.getName());
            coincomma = Helper.getFormattedText(StaticConfig.user.getCoins());
            txtcoins.setText(coincomma);
            coincomma = Helper.getFormattedText(StaticConfig.user.getReceivedCoins());
            txtrcvcoins.setText(coincomma);
            PhotoUrl = StaticConfig.user.getImageurl();
            Glide.with(ProfileActivity.this).load(StaticConfig.user.getImageurl()).into(userimg);
        }
    }

    public void refreshtoken(View view) {

        Log.e("valueee", FirebaseAuth.getInstance().getCurrentUser().getUid() + "" + "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("cJupIaBOKXN8QqWzAQMQYFwHzVC3"))
            if (getSharedPreferences("auxilium", MODE_PRIVATE).getInt("dayofmonth", 0) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                Log.e("valueee", "today  " + "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
                getSharedPreferences("auxilium", MODE_PRIVATE).edit().putInt("dayofmonth", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).apply();
                Toast.makeText(this, "Please wait token going refresh, this is take place one time in  a day", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("AllRooms").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            gettoken(dataSnapshot.getKey());
                            Log.e("valueee", dataSnapshot.getKey() + "    " + "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
    }

    public void editprofile(View view) {
        startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
    }

    public void Walllet(View view) {
        startActivity(new Intent(ProfileActivity.this, WalletActivity.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        try {
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
        } catch (Exception e) {
            System.out.println(e);
        }
        Intent intent = new Intent(ProfileActivity.this, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onClickJoin(View view) {
        roomcheck();
    }

    public void myProfile(View view) {
        finish();

    }

    public void roomcheck() {
        startActivity(new Intent(ProfileActivity.this, EnterRoomActivity.class));
    }

}