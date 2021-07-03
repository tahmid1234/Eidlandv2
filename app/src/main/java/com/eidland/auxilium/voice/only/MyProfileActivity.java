package com.eidland.auxilium.voice.only;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import io.agora.rtc.Constants;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Staticconfig;
import com.eidland.auxilium.voice.only.ui.LiveRoomActivity;
import com.eidland.auxilium.voice.only.ui.WalletActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {
    ImageView userimg;
    LinearLayout button_join, lrnrefrsh;
    String userid;
    TextView txtname, txtcoins, txtrcvcoins;
    DecimalFormat formatter;
    String finalText, coincomma;
    FirebaseAuth mAuth;
    Boolean edited=false;
    String PhotoUrl;

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
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Uri ir = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (userid.equals("cJupIaBOKXN8QqWzAQMQYFwHzVC3")) {
            lrnrefrsh.setVisibility(View.VISIBLE);
        }
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
        Uri img1 = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();


    }

    public void gettoken(final String roomname) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(MyProfileActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, "https://auxilium2.herokuapp.com/access_token?channel=" + roomname, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("token");
                    FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomname).child("token").setValue(token);
                    Toast.makeText(MyProfileActivity.this, roomname + " token added", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Log.e("roor", e.getLocalizedMessage() + "d");
                    //  Toast.makeText(MainActivity.this, "Json", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
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

    public String formattedtext(String coin) {
        BigDecimal val = new BigDecimal(coin);
        formatter = new DecimalFormat("#,###,###");
        finalText = formatter.format(val);
        return finalText;
    }

    public void finishthis(View view) {

        Uri img = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        User obj = new User(Staticconfig.user.getName(), Staticconfig.user.getEmail(), PhotoUrl, Staticconfig.user.getCoins(), Staticconfig.user.getReceivedCoins());
        Staticconfig.user = obj;

        Intent intent = new Intent(MyProfileActivity.this, LiveRoomActivity.class);
        intent.putExtra("User", "Participent");
        intent.putExtra("userid", "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
        intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, "760232943A3qP5qyS34aGkFxQa3caaXxmHGl2");

        intent.putExtra("UserName", "Eidland Battle Royale");
        intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");

        intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Staticconfig.user != null) {
            txtname.setText(Staticconfig.user.getName());
            coincomma = formattedtext(Staticconfig.user.getCoins());
            txtcoins.setText(coincomma);
            coincomma = formattedtext(Staticconfig.user.getReceivedCoins());
            txtrcvcoins.setText(coincomma);
            PhotoUrl=Staticconfig.user.getImageurl();
            Glide.with(MyProfileActivity.this).load(Staticconfig.user.getImageurl()).into(userimg);
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

        startActivity(new Intent(MyProfileActivity.this, MyProfile.class));
    }

    public void Walllet(View view) {
        startActivity(new Intent(MyProfileActivity.this, WalletActivity.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        try {
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
        }catch (Exception e){
            System.out.println(e);
        }
        Intent intent = new Intent(MyProfileActivity.this, Sign_Up_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onClickJoin(View view) {
        // show dialog to choose role
        roomcheck();

    }

    public void myProfile(View view) {
        finish();

    }

    public void roomcheck() {

        startActivity(new Intent(MyProfileActivity.this, EnterRoomActivity.class));

//

    }
}