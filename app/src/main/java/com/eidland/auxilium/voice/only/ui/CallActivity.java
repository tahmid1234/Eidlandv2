package com.eidland.auxilium.voice.only.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.adapter.ViewerAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eidland.auxilium.voice.only.AGApplication;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.User;
import com.eidland.auxilium.voice.only.adapter.CommentAdapter;
import com.eidland.auxilium.voice.only.model.AGEventHandler;
import com.eidland.auxilium.voice.only.model.Comment;
import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Staticconfig;
import com.eidland.auxilium.voice.only.model.Viewer;
import com.eidland.auxilium.voice.only.ui.RoomsRecycler.Rooms;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class CallActivity extends AppCompatActivity implements AGEventHandler, View.OnClickListener {
    String Seats, type, UserName, SeatsName, AgainSeat, leave = null, run;
    LinearLayout _seat0, _seat1, _seat2, _seat3, _seat4, _seat5, _seat6, _seat7, _seat8, _seat9;
    TextView _host_name, _name1, _name2, _name3, _name4, _name5, _name6, _name7, _name8, _name9, broadname, txtsendgift, txtusercoin;
    ImageView _image0, _image1, _image2, _image3, _image4, _image5, _image6, _image7, _image8, _image9, sencmnt;
    ProgressDialog progressDialog;
    ImageView button2, imgbroad;
    ImageView button1;
    String pushid = "";
    ArrayList<Viewer> viewerslist;
    ViewerAdapter viewerAdapter;
    String hostuid;
    Spinner spinner;
    String selectuseruid;
    EditText txtcmnt;
    private final static Logger log = LoggerFactory.getLogger(CallActivity.class);

    private volatile boolean mAudioMuted = false;

    private volatile int mAudioRouting = -1; // Default
    ChildEventListener eventListener;
    String imgurl;
    RecyclerView viewers;
    ImageView bottom_action_end_call;
    RecyclerView recyclerView;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
    ImageView iv200flower, iv500hearts, iv1000cake, iv10kladiesbag, iv15happy, iv20giftpack, iv25kheartcake, iv25kband, iv30kneckless, iv40kring;
    ImageView iv50kbucket, iv50earring, iv50kking, iv50queen, btngift, closegift, close, singleimg;
    LinearLayout crystal;
    TextView txtsinglename, txtsinglegiftsend;
    DatabaseReference userref;
    RelativeLayout singl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...!");
        progressDialog.setMessage("Processing Data...!");
        progressDialog.setCancelable(false);
        button2 = findViewById(R.id.mute_local_speaker_id);
        button1 = findViewById(R.id.switch_broadcasting_id);
        bottom_action_end_call = findViewById(R.id.bottom_action_end_call);
        txtsinglegiftsend = findViewById(R.id.singlegiftsend);
        txtsinglename = findViewById(R.id.singlename);
        singleimg = findViewById(R.id.singleimg);
        close = findViewById(R.id.close);
        singl = findViewById(R.id.single_user_box);
        viewers = findViewById(R.id.viewersrecyler);
        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        txtsendgift = findViewById(R.id.send_gift);
        txtusercoin = findViewById(R.id.user_available_coin);
        spinner = findViewById(R.id.spinner);
        crystal = findViewById(R.id.giftslayout);
        closegift = findViewById(R.id.closegift);
        btngift = findViewById(R.id.room_gift);
        txtcmnt = findViewById(R.id.comment_box);
        sencmnt = findViewById(R.id.sndcmnt);
        _seat0 = findViewById(R.id._seat0);
        imgbroad = findViewById(R.id.hostimg);
        broadname = findViewById(R.id.room_name);
        _host_name = findViewById(R.id._host_name0);
        _image0 = findViewById(R.id._image0);
        _seat1 = findViewById(R.id._seat1);
        _name1 = findViewById(R.id._host_name1);
        _image1 = findViewById(R.id._image1);
        _seat2 = findViewById(R.id._seat2);
        _name2 = findViewById(R.id._host_name2);
        _image2 = findViewById(R.id._image2);
        _seat3 = findViewById(R.id._seat3);
        _name3 = findViewById(R.id._host_name3);
        _image3 = findViewById(R.id._image3);
        _seat4 = findViewById(R.id._seat4);
        _name4 = findViewById(R.id._host_name4);
        _image4 = findViewById(R.id._image4);
        _seat5 = findViewById(R.id._seat5);
        _name5 = findViewById(R.id._host_name5);
        _image5 = findViewById(R.id._image5);
        _seat6 = findViewById(R.id._seat6);
        _name6 = findViewById(R.id._host_name6);
        _image6 = findViewById(R.id._image6);
        _seat7 = findViewById(R.id._seat7);
        _name7 = findViewById(R.id._host_name7);
        _image7 = findViewById(R.id._image7);
        _seat8 = findViewById(R.id._seat8);
        _name8 = findViewById(R.id._host_name8);
        _image8 = findViewById(R.id._image8);
        _seat9 = findViewById(R.id._seat9);
        _name9 = findViewById(R.id._host_name9);
        _image9 = findViewById(R.id._image9);
        iv200flower = findViewById(R.id.iv200redflower);
        iv500hearts = findViewById(R.id.iv500hearts);
        iv1000cake = findViewById(R.id.iv1000cake);
        iv15happy = findViewById(R.id.iv15khappybirthday);
        iv20giftpack = findViewById(R.id.iv20kgift);
        iv25kheartcake = findViewById(R.id.iv25kheartcake);
        iv25kband = findViewById(R.id.handband);
        iv30kneckless = findViewById(R.id.neckless);
        iv40kring = findViewById(R.id.dimond);
        iv50queen = findViewById(R.id.queen);
        iv50kking = findViewById(R.id.king);
        iv50earring = findViewById(R.id.earing);
        iv50kbucket = findViewById(R.id.flowerbuckt);
        iv10kladiesbag = findViewById(R.id.iv10kbag);
        closegift.setOnClickListener(this);
        btngift.setOnClickListener(this);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singl.setVisibility(View.GONE);
            }
        });
        txtusercoin.setOnClickListener(this);
        txtsendgift.setOnClickListener(this);
        iv200flower.setOnClickListener(this);
        iv500hearts.setOnClickListener(this);

        iv1000cake.setOnClickListener(this);
        // iv10kgentwatch.setOnClickListener(this);
        iv10kladiesbag.setOnClickListener(this);
        iv15happy.setOnClickListener(this);
        _seat0.setOnClickListener(this);
        iv20giftpack.setOnClickListener(this);
        iv25kheartcake.setOnClickListener(this);
        iv25kband.setOnClickListener(this);
        iv30kneckless.setOnClickListener(this);
        iv40kring.setOnClickListener(this);
        iv50queen.setOnClickListener(this);
        iv50kking.setOnClickListener(this);
        iv50earring.setOnClickListener(this);
        iv50kbucket.setOnClickListener(this);

        _seat1.setOnClickListener(this);
        _seat2.setOnClickListener(this);
        _seat3.setOnClickListener(this);
        _seat4.setOnClickListener(this);
        _seat5.setOnClickListener(this);
        _seat6.setOnClickListener(this);
        _seat7.setOnClickListener(this);
        _seat8.setOnClickListener(this);
        _seat9.setOnClickListener(this);
        lastimg = iv200flower;
        comments = new ArrayList<>();
        recyclerView = findViewById(R.id.live_comment_recyler);
        final Comment comment = new Comment();
        comment.setComment("Nice to meet you ");
        comment.setName("Auxilium");
        comments.add(comment);
        commentAdapter = new CommentAdapter(this, comments, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, final int position) {
                if (position == 0) {
                    Toast.makeText(CallActivity.this, "This is auto Generated Text for every live", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            /*    if(isfirstinit) {
                    isfirstinit=false;
                }
                else{
                    for (DataSnapshot childd : dataSnapshot.getChildren()) {*/
                Viewer viewer = dataSnapshot.getValue(Viewer.class);
                boolean isexist = false;
                for (int i = 0; i < viewerslist.size(); i++) {
                    if (viewerslist.get(i).getUid().equals(viewer.getUid())) {
                        isexist = true;
                        break;
                    }

                }

                if (!isexist) {
                    viewerslist.add(viewer);
                    //  talentviewers.setText(viewerslist.size() + "");
                 /*   if (viewer.getType() != null)
                        if (!viewer.getType().equals(StaticConfig.STR_TYPE)) {
                            Viewerlist.add(new Viewer(viewer.getId(), viewer.getPhoto(), viewer.getType()));
                            animforsuper(viewer.getPhoto());

                        }
*/
                    viewerAdapter.notifyDataSetChanged();
                }
                //  }


                //}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                for (int i = 0; i < viewerslist.size(); i++) {
                    if (viewerslist.get(i).getUid().equals(dataSnapshot.getValue(Viewer.class).getUid())) {
                        viewerslist.remove(i);
                        break;
                    }

                }
                // talentviewers.setText(viewerslist.size()+"");
                viewerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        viewerslist = new ArrayList<>();
        viewers.hasFixedSize();
        viewers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        viewerAdapter = new ViewerAdapter(CallActivity.this, viewerslist, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, int position) {
            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        viewers.setAdapter(viewerAdapter);

        type = getIntent().getStringExtra("User");

        UserName = getIntent().getStringExtra("UserName");
        imgurl = getIntent().getStringExtra("profile");
        Glide.with(this).load(imgurl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(imgbroad);
        broadname.setText(UserName);
       /* Glide.with(this).load(imgurl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(_image0);
        _host_name.setText(UserName);*/
        if (type.equals("Host")) {
            hostuid = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgurl,Staticconfig.user.getEmail(), "host");
            pushid = FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).child(pushid).setValue(viewer);
            AgainSeat = "seat0";
            gettoken();
        } else {
            hostuid = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            Viewer comment1 = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(),Staticconfig.user.getEmail(), Staticconfig.user.getEmail());
            pushid = FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).child(pushid).setValue(comment1);
            setNameAllSeats();
            inist(getIntent().getStringExtra("token"));
        }
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).addChildEventListener(eventListener);
        sencmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(txtcmnt.getText().toString())) {
                    Comment comment1 = new Comment(Staticconfig.user.getName(), txtcmnt.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid() ,false, ".", ".",Staticconfig.user.getImageurl());

                    FirebaseDatabase.getInstance().getReference().child("livecomments").child(hostuid).push().setValue(comment1);
                    txtcmnt.setText("");
                }
            }
        });
        selectuseruid = hostuid;
        txtsinglegiftsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singl.setVisibility(View.GONE);
                crystal.setVisibility(View.VISIBLE);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).child(pushid).onDisconnect().removeValue();
        txtusercoin.setText(Staticconfig.user.getCoins() + "");
        userref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Staticconfig.user=user;
                txtusercoin.setText(Staticconfig.user.getCoins() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inist(String token) {
     //   event().addEventHandler(this);

        Intent i = getIntent();

        int cRole = i.getIntExtra(ConstantApp.ACTION_KEY_CROLE, 0);

        if (cRole == 0) {
            throw new RuntimeException("Should not reach here");
        }

        String roomName = i.getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);

        doConfigEngine(cRole);

        ImageView button1 = findViewById(R.id.switch_broadcasting_id);


        if (isBroadcaster(cRole)) {
            broadcasterUI(button1, button2);
        } else {
            audienceUI(button1, button2);
        }

        ((AGApplication) getApplication()).getWorkerThread().joinChannel(token, roomName, ((AGApplication) getApplication()).getWorkerThread().getEngineConfig().mUid);

        TextView textRoomName = findViewById(R.id.room_name);
        // textRoomName.setText("CS & IT  Room");

        optional();

       /* LinearLayout bottomContainer = (LinearLayout) findViewById(R.id.lrnbottom);
        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();
        fmp.bottomMargin = virtualKeyHeight() + 16;*/
    }

    public void joinChannel(String token) {
        Rooms room = new Rooms(UserName, imgurl, hostuid, token,"0","");
        FirebaseDatabase.getInstance().getReference().child("AllRooms").child(hostuid).setValue(room);
        SeatsName = "seat0";
        Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(),imgurl, Staticconfig.user.getEmail(),UserName );
        // ali
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(SeatsName).setValue(viewer);
        AgainSeat=SeatsName;
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(SeatsName).onDisconnect().removeValue();
        setNameAllSeats();
        inist(token);

    }

    public void gettoken() {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(CallActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, "http://auxiliumlivestreaming.000webhostapp.com/token/php/RtcTokenBuilderSample.php?channel=" + hostuid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("token");

                    joinChannel(token);

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Log.e("roor", e.getLocalizedMessage() + "d");
                    Toast.makeText(CallActivity.this, "Json", Toast.LENGTH_SHORT).show();
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

    private void setNameAllSeats() {
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Viewer seat0 = snapshot.child("seat0").getValue(Viewer.class);
                    Viewer seat1 = snapshot.child("seat1").getValue(Viewer.class);
                    Viewer seat2 = snapshot.child("seat2").getValue(Viewer.class);
                    Viewer seat3 = snapshot.child("seat3").getValue(Viewer.class);
                    Viewer seat4 = snapshot.child("seat4").getValue(Viewer.class);
                    Viewer seat5 = snapshot.child("seat5").getValue(Viewer.class);
                    Viewer seat6 = snapshot.child("seat6").getValue(Viewer.class);
                    Viewer seat7 = snapshot.child("seat7").getValue(Viewer.class);
                    Viewer seat8 = snapshot.child("seat8").getValue(Viewer.class);
                    Viewer seat9 = snapshot.child("seat9").getValue(Viewer.class);
                    if (seat0 != null) {
                        _host_name.setText(seat0.getType());
                        Glide.with(getApplicationContext()).load(seat0.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image0);
                    } else {
                        _host_name.setText("Seat #0");
                        _image0.setImageResource(R.drawable.userprofile);

                    }

                    if (seat1 != null) {
                        _name1.setText(seat1.getType());
                        Glide.with(getApplicationContext()).load(seat1.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image1);
                    } else {
                        _name1.setText("Seat #1");
                        _image1.setImageResource(R.drawable.userprofile);

                    }
                    if (seat2 != null) {
                        _name2.setText(seat2.getType());
                        Glide.with(getApplicationContext()).load(seat2.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image2);

                    } else {
                        _name2.setText("Seat #2");
                        _image2.setImageResource(R.drawable.userprofile);

                    }
                    if (seat3 != null) {
                        _name3.setText(seat3.getType());
                        Glide.with(getApplicationContext()).load(seat3.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image3);

                    } else {
                        _name3.setText("Seat #3");
                        _image3.setImageResource(R.drawable.userprofile);

                    }
                    if (seat4 != null) {
                        _name4.setText(seat4.getType());
                        Glide.with(getApplicationContext()).load(seat4.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image4);

                    } else {
                        _name4.setText("Seat #4");
                        _image4.setImageResource(R.drawable.userprofile);

                    }
                    if (seat5 != null) {
                        _name5.setText(seat5.getType());
                        Glide.with(getApplicationContext()).load(seat5.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image5);

                    } else {
                        _name5.setText("Seat #5");
                        _image5.setImageResource(R.drawable.userprofile);

                    }
                    if (seat6 != null) {
                        _name6.setText(seat6.getType());
                        Glide.with(getApplicationContext()).load(seat6.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image6);

                    } else {
                        _name6.setText("Seat #6");
                        _image6.setImageResource(R.drawable.userprofile);

                    }
                    if (seat7 != null) {
                        _name7.setText(seat7.getType());
                        Glide.with(getApplicationContext()).load(seat7.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image7);

                    } else {
                        _name7.setText("Seat #7");
                        _image7.setImageResource(R.drawable.userprofile);

                    }
                    if (seat8 != null) {
                        _name8.setText(seat8.getType());
                        Glide.with(getApplicationContext()).load(seat8.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image8);

                    } else {
                        _name8.setText("Seat #8");
                        _image8.setImageResource(R.drawable.userprofile);

                    }
                    if (seat9 != null) {
                        _name9.setText(seat9.getType());
                        Glide.with(getApplicationContext()).load(seat9.getPhotoUrl()).placeholder(R.drawable.userprofile).into(_image9);
                    } else {
                        _name9.setText("Seat #9");
                        _image9.setImageResource(R.drawable.userprofile);

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Seat_1(View view) {
        if (type.equals("Participent")) {
            if (AgainSeat == null) {
                CheckSeats("seat1");
            } else {
                toast();
            }
        }
    }

    public void Seat_2(View view) {
        if (type.equals("Participent")) {
            if (AgainSeat == null) {
                CheckSeats("seat2");
            } else {
                toast();
            }
        }
    }

    public void Seat_3(View view) {
        if (type.equals("Participent")) {

        }
    }

    private void toast() {
        // Toast.makeText(this, "You have Already a room", Toast.LENGTH_SHORT).show();
    }

    private void CheckSeats(final String seats) {
        if (AgainSeat == null) {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getUid();
                        Glide.with(getApplicationContext()).load(viewer.getPhotoUrl()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(singleimg);
                        txtsinglename.setText(viewer.getType());
                        singl.setVisibility(View.VISIBLE);
                    } else {

                        Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(),Staticconfig.user.getEmail(), Staticconfig.user.getName());
                        FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(seats).setValue(viewer);

                        FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(seats).onDisconnect().removeValue();
                        doSwitchToBroadcaster(true);
                        AgainSeat = seats;

                        //  button2.setVisibility(View.GONE);

                        //ImageView button2 = (ImageView) findViewById(R.id.mute_local_speaker_id);
                        // broadcasterUI(button1, button2);*/


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getUid();
                        Glide.with(getApplicationContext()).load(viewer.getPhotoUrl()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(singleimg);
                        txtsinglename.setText(viewer.getType());
                        singl.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            toast();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }



    private Handler mMainHandler;

    private static final int UPDATE_UI_MESSAGE = 0x1024;


    StringBuffer mMessageCache = new StringBuffer();

    private void notifyMessageChanged(String msg) {
        if (mMessageCache.length() > 10000) { // drop messages
            mMessageCache = new StringBuffer(mMessageCache.substring(10000 - 40));
        }

        mMessageCache.append(System.currentTimeMillis()).append(": ").append(msg).append("\n"); // append timestamp for messages

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mMainHandler == null) {
                    mMainHandler = new Handler(getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);

                            if (isFinishing()) {
                                return;
                            }

                            switch (msg.what) {
                                case UPDATE_UI_MESSAGE:
                                    String content = (String) (msg.obj);
                                    break;

                                default:
                                    break;
                            }

                        }
                    };

                }

                mMainHandler.removeMessages(UPDATE_UI_MESSAGE);
                Message envelop = new Message();
                envelop.what = UPDATE_UI_MESSAGE;
                envelop.obj = mMessageCache.toString();
                mMainHandler.sendMessageDelayed(envelop, 1000l);
            }
        });
    }

    private void optional() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onSwitchSpeakerClicked(View view) {
        log.info("onSwitchSpeakerClicked " + view + " " + mAudioMuted + " " + mAudioRouting);

        RtcEngine rtcEngine = ((AGApplication) getApplication()).getWorkerThread().getRtcEngine();
        // Enables/Disables the audio playback route to the speakerphone.
        // This method sets whether the audio is routed to the speakerphone or earpiece. After calling this method, the SDK returns the onAudioRouteChanged callback to indicate the changes.
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3);
    }

    private void doConfigEngine(int cRole) {
        ((AGApplication) getApplication()).getWorkerThread().configEngine(cRole);
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

 /*   private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }*/

  /*  @Override
    protected void deInitUIandEvent() {
        optionalDestroy();

        doLeaveChannel();
        event().removeEventHandler(this);
    }*/

  /*  private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);

    }*/

    public void onEndCallClicked(View view) {
        leave = "dialoge";
        End1();
    }

    @Override
    public void onBackPressed() {
        leave = "dialoge";
        End1();


    }

    private void End1() {
        if (leave.equals("dialoge")) {
            AlertDialog.Builder dialoge = new AlertDialog.Builder(CallActivity.this);
            dialoge.setTitle("Confirm Exit..!!")
                    .setMessage("Are You sure You want to leave meating")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            run = "1";
                            progressDialog.show();
                            EndMeeting();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {
            progressDialog.show();
            EndMeeting();
        }
    }

    private void EndMeeting() {
        /*    if (type.equals("Host")) {
         *//* FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child("Room").setValue("Room Created")*//*;
        }else {*/
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(hostuid).child(pushid).removeValue();
        //   }

        if (AgainSeat != null) {
            FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(AgainSeat).removeValue();
            log.info("onBackPressed");
            progressDialog.cancel();
            finish();
            CallActivity.super.onBackPressed();
            run = "1";

        } else {
            progressDialog.cancel();
            finish();
            CallActivity.super.onBackPressed();
            run = "1";
        }


    }






    private void doSwitchToBroadcaster(boolean broadcaster) {
        final int uid = ((AGApplication) getApplication()).getWorkerThread().getEngineConfig().mUid;
        log.debug("doSwitchToBroadcaster " + (uid & 0XFFFFFFFFL) + " " + broadcaster);

        if (broadcaster) {
            doConfigEngine(Constants.CLIENT_ROLE_BROADCASTER);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ImageView button1 = findViewById(R.id.switch_broadcasting_id);
                    ImageView button2 = findViewById(R.id.mute_local_speaker_id);
                    broadcasterUI(button1, button2);
                }
            }, 1000); // wait for reconfig engine
        } else {
            stopInteraction(uid);
        }
    }

    private void stopInteraction(final int uid) {
        doConfigEngine(Constants.CLIENT_ROLE_AUDIENCE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView button1 = findViewById(R.id.switch_broadcasting_id);
                ImageView button2 = findViewById(R.id.mute_local_speaker_id);
                audienceUI(button1, button2);
            }
        }, 1000); // wait for reconfig engine
    }

    private void audienceUI(ImageView button1, ImageView button2) {
        button1.setTag(null);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                doSwitchToBroadcaster(tag == null || !((boolean) tag));
            }
        });
        button1.clearColorFilter();
        button2.setTag(null);
        button2.setVisibility(View.GONE);
        bottom_action_end_call.setVisibility(View.GONE);
        button2.clearColorFilter();
    }

    private void broadcasterUI(ImageView button1, ImageView button2) {
        button1.setTag(true);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                doSwitchToBroadcaster(tag == null || !((boolean) tag));
            }
        });
        button1.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                boolean flag = true;
                if (tag != null && (boolean) tag) {
                    flag = false;
                }
                ((AGApplication) getApplication()).getWorkerThread().getRtcEngine().muteLocalAudioStream(flag);
                ImageView button = (ImageView) v;
                button.setTag(flag);
                if (flag) {
                    button.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
                } else {
                    button.clearColorFilter();
                }
            }
        });

        button2.setVisibility(View.VISIBLE);
        bottom_action_end_call.setVisibility(View.VISIBLE);
    }

    public void onVoiceMuteClicked(View view) {
        log.info("onVoiceMuteClicked " + view + " audio_status: " + mAudioMuted);

        RtcEngine rtcEngine =((AGApplication) getApplication()).getWorkerThread().getRtcEngine();
        // Stops/Resumes sending the local audio stream.
        // A successful muteLocalAudioStream method call triggers the onUserMuteAudio callback on the remote client.
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        if (mAudioMuted) {
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        String msg = "onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed;
        log.debug(msg);

        notifyMessageChanged(msg);
        FirebaseDatabase.getInstance().getReference().child("livecomments").child(hostuid).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childd : dataSnapshot.getChildren()) {
                    //This might work but it retrieves all the data
                    comments.add(childd.getValue(Comment.class));
                }
                commentAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(comments.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        String msg = "onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason;
        log.debug(msg);

        notifyMessageChanged(msg);

    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED: {
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                notifyMessageChanged("mute: " + (peerUid & 0xFFFFFFFFL) + " " + muted);
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_QUALITY: {
                peerUid = (Integer) data[0];
                int quality = (int) data[1];
                short delay = (short) data[2];
                short lost = (short) data[3];

                notifyMessageChanged("quality: " + (peerUid & 0xFFFFFFFFL) + " " + quality + " " + delay + " " + lost);
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS: {
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                StringBuilder volumeCache = new StringBuilder();
                for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                    peerUid = each.uid;
                    int peerVolume = each.volume;

                    if (peerUid == 0) {
                        continue;
                    }

                    volumeCache.append("volume: ").append(peerUid & 0xFFFFFFFFL).append(" ").append(peerVolume).append("\n");
                }

                if (volumeCache.length() > 0) {
                    String volumeMsg = volumeCache.substring(0, volumeCache.length() - 1);
                    notifyMessageChanged(volumeMsg);

                    if ((System.currentTimeMillis() / 1000) % 10 == 0) {
                        log.debug(volumeMsg);
                    }
                }
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR: {
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                  //  showLongToast(getString(R.string.msg_no_network_connection));
                }

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];

                notifyMessageChanged(error + " " + description);

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED: {
                notifyHeadsetPlugged((int) data[0]);

                break;
            }
        }
    }

    public void notifyHeadsetPlugged(final int routing) {
        log.info("notifyHeadsetPlugged " + routing);

        mAudioRouting = routing;

        ImageView iv = findViewById(R.id.switch_speaker_id);
        if (mAudioRouting == 3) { // Speakerphone
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }


    public void leave(View view) {
        log.info("onEndCallClicked " + view);
        if (run == null) {
            leave = "dialoge";
            End1();
        }
    }

    ImageView lastimg;

    public void setselct(ImageView s) {
        lastimg.setImageResource(0);
        lastimg = s;
        s.setImageResource(R.drawable.selectedicon);
        switch (s.getId()) {
            case R.id.iv500hearts:
                selectedgiftname = "hearts";
                break;
            case R.id.iv200redflower:
                selectedgiftname = "flower";

                break;
            case R.id.iv1000cake:
                selectedgiftname = "cake";

                break;
            case R.id.iv15khappybirthday:
                selectedgiftname = "happy";

                break;
            case R.id.iv20kgift:
                selectedgiftname = "box";

                break;
            case R.id.iv25kheartcake:
                selectedgiftname = "heartcake";

                break;

            case R.id.neckless:
                selectedgiftname = "neckless";

                break;
            case R.id.dimond:
                selectedgiftname = "ring";

                break;
            case R.id.queen:
                selectedgiftname = "queen";


                break;
            case R.id.king:
                selectedgiftname = "king";


                break;
            case R.id.earing:
                selectedgiftname = "earring";


                break;

            case R.id.flowerbuckt:
                selectedgiftname = "bucket";


                break;
            case R.id.iv10kbag:
                selectedgiftname = "bag";

                break;

            case R.id.handband:
                selectedgiftname = "band";


                break;
        }
    }

    int selectamnt = 200;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id._seat0:

                CheckSeats("seat0");
                break;
            case R.id._seat1:
                CheckSeats("seat1");
                break;
            case R.id._seat2:
                CheckSeats("seat2");
                break;
            case R.id._seat3:
                CheckSeats("seat3");
                break;
            case R.id._seat4:
                CheckSeats("seat4");
                break;
            case R.id._seat5:
                CheckSeats("seat5");
                break;
            case R.id._seat6:
                CheckSeats("seat6");
                break;
            case R.id._seat7:
                CheckSeats("seat7");
                break;
            case R.id._seat8:
                CheckSeats("seat8");
                break;
            case R.id._seat9:
                CheckSeats("seat9");
                break;

            case R.id.closegift:
                crystal.setVisibility(View.GONE);
                break;
            case R.id.room_gift:
                selectuseruid = hostuid;
                txtsinglename.setText(UserName);
                crystal.setVisibility(View.VISIBLE);
                break;
            case R.id.iv500hearts:
                setselct(iv500hearts);
                selectamnt = 500;
                break;
            case R.id.iv200redflower:

                setselct(iv200flower);
                selectamnt = 200;
                break;
            case R.id.iv1000cake:

                setselct(iv1000cake);

                selectamnt = 1000;
                break;
            case R.id.iv15khappybirthday:
                setselct(iv15happy);

                selectamnt = 15000;

                break;
            case R.id.iv20kgift:
                setselct(iv20giftpack);

                selectamnt = 20000;

                break;
            case R.id.iv25kheartcake:
                setselct(iv25kheartcake);

                selectamnt = 25000;
                break;

            case R.id.neckless:
                setselct(iv30kneckless);

                selectamnt = 30000;
                break;
            case R.id.dimond:
                setselct(iv40kring);

                selectamnt = 40000;
                break;
            case R.id.queen:
                setselct(iv50queen);


                selectamnt = 50000;

                break;
            case R.id.king:
                setselct(iv50kking);

                selectamnt = 50000;

                break;

            case R.id.earing:
                setselct(iv50earring);

                selectamnt = 50000;


                break;

            case R.id.flowerbuckt:
                setselct(iv50kbucket);

                selectamnt = 50000;

                break;
            case R.id.iv10kbag:
                setselct(iv10kladiesbag);

                selectamnt = 10000;
                break;

            case R.id.handband:
                setselct(iv25kband);
                selectamnt = 25000;

                break;
            case R.id.send_gift:
                if(!selectuseruid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (selectamnt > 0) {
                        Long curnt = Long.parseLong(Staticconfig.user.getCoins());
                        if (curnt > selectamnt) {
                            crystal.setVisibility(View.GONE);
                            userref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    long l = Long.parseLong(user.getCoins());
                                    l = l - selectamnt;
                                    user.setCoins(l + "");
                                    userref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                    Staticconfig.user.setCoins(l + "");
                                    txtusercoin.setText(l + "");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            userref.child(selectuseruid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    long l = Long.parseLong(user.getCoins());
                                    l = l + selectamnt;
                                    user.setCoins(l + "");
                                    userref.child(selectuseruid).setValue(user);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            sendgift();
                        } else
                            Toast.makeText(this, "Low Balance Please Purchase Coins", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "No Gift is selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "You can not send gift to yourself", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.user_available_coin:
                startActivity(new Intent(CallActivity.this, WalletActivity.class));

                break;


        }

    }

    String selectedgiftname = "flowers";

    private void sendgift() {
        Comment comment = new Comment(Staticconfig.user.getName(), "Sent " + selectedgiftname + " to " + txtsinglename.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), true, selectedgiftname, "1",Staticconfig.user.getImageurl());
        FirebaseDatabase.getInstance().getReference().child("livecomments").child(hostuid).push().setValue(comment);

    }
}