package com.eidland.auxilium.voice.only.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eidland.auxilium.voice.only.AGApplication;
import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.MyProfileActivity;
import com.eidland.auxilium.voice.only.adapter.CommentAdapter;
import com.eidland.auxilium.voice.only.adapter.ViewerAdapter;
import com.eidland.auxilium.voice.only.adapter.ViewerListAdapter;
import com.eidland.auxilium.voice.only.model.AGEventHandler;
import com.eidland.auxilium.voice.only.model.Comment;
import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Gift;
import com.eidland.auxilium.voice.only.model.Staticconfig;
import com.eidland.auxilium.voice.only.model.Viewer;
import com.eidland.auxilium.voice.only.ui.RoomsRecycler.Rooms;
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.User;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import pl.droidsonroids.gif.GifImageView;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class LiveRoomActivity extends BaseActivity implements AGEventHandler, View.OnClickListener {
    String Seats, type, UserName, SeatsName, AgainSeat, leave = null, run;
    LinearLayout _seat0, _seat1, _seat2, _seat3, _seat4, _seat5, _seat6, _seat7, _seat8, _seat9;
    TextView txtviewerscount,_host_name, _name1, _name2, _name3, _name4, _name5, _name6, _name7, _name8, _name9, broadname, txtsendgift, txtusercoin;
    ImageView _image0, _image1, _image2, _image3, _image4, _image5, _image6, _image7, _image8, _image9,_image10, _image11, _image12, _image13, sencmnt;
    ProgressDialog progressDialog;
    LinearLayout showusers;
    TextView ModUserRemove;
    Boolean muteClicked=false;
    String SelfUid;
    ImageView button2, imgbroad;
    ImageView button1;
    String pushid = "";
    DecimalFormat formatter;
    String finalText,coincomma;
    ArrayList<Viewer> viewerslist;
    ViewerAdapter viewerAdapter;
    String hostuid,roomname;
    Spinner spinner;
    String selectuseruid;
    EditText txtcmnt;
    String Clickedseat=null;
    Animation enter;
    Animation exit;
    private final static Logger log = LoggerFactory.getLogger(LiveRoomActivity.class);
    CircleImageView popup_user,commentuser;
    TextView popup_uname;
    private volatile boolean mAudioMuted = false;
    ImageView userimage;
    private volatile int mAudioRouting = -1; // Default
    ChildEventListener eventListener;
    String imgurl;
    Boolean Moderator;
    RecyclerView viewers,viewerlist;
    ImageView bottom_action_end_call;
    RecyclerView recyclerView;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
    ViewerListAdapter viewerListAdapter;
    ImageView iv200flower, iv500hearts, ivpigions, ivoscar, iv1000cake, iv10kladiesbag, iv15happy, iv20giftpack, iv25kheartcake, iv25kband, iv30kneckless, iv40kring;
    ImageView iv50kbucket, iv50earring, iv50kking, iv50queen, btngift, closegift, close, singleimg,senderimg;
    LinearLayout crystal;
    TextView txtsinglename, txtsinglegiftsend,sendername,receivername;
    RelativeLayout singlegift;
    DatabaseReference userref;
    RelativeLayout singl;
    RelativeLayout confettiLayout;
    ImageView button;
    LinearLayout contentView;
    Animation enterAnimation, leaveAnimation;
    TextView giftAnimation;
    RelativeLayout animatedlayout;
    GifImageView simplegift;
    GifImageView confetti;
    boolean flag;
    ArrayList<Gift> giftslist;
    boolean isKeyboardShowing = false;
    void onKeyboardVisibilityChanged(boolean opened) {
        Toast.makeText(this, "keyboard"+opened, Toast.LENGTH_SHORT).show();
    }
    int height,width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels/2;
        height=height-150;
        width = displayMetrics.widthPixels;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Your Room is being ready..");
        progressDialog.setCancelable(false);
        userimage=findViewById(R.id._userchatroom);
        button2 = (ImageView)findViewById(R.id.mute_local_speaker_id);
        button1 = (ImageView)findViewById(R.id.switch_broadcasting_id);
        ModUserRemove=findViewById(R.id.removeUser);
        bottom_action_end_call = (ImageView)findViewById(R.id.bottom_action_end_call);
        txtviewerscount = findViewById(R.id.txtviewerscount);
        txtsinglegiftsend = findViewById(R.id.singlegiftsend);
        singlegift=findViewById(R.id.singlesendgift);
        txtsinglename = findViewById(R.id.txtnamepopup);
        singleimg = findViewById(R.id.singleimg);
        close = findViewById(R.id.close);
        simplegift = findViewById(R.id.imggif);
        confetti = findViewById(R.id.confetti);
        confettiLayout = findViewById(R.id.confettiLayout);
        sendername = findViewById(R.id.sendername);
        receivername = findViewById(R.id.receivername);
        popup_uname=findViewById(R.id.txtnamepopup);
        popup_user=findViewById(R.id.userimgpopup);
        showusers=findViewById(R.id.showonlineusers);
      //  senderimg = findViewById(R.id.senderimg);
        animatedlayout = findViewById(R.id.animatedlayout);
        giftslist=new ArrayList<>();
        singl = findViewById(R.id.reltivesingle);
        viewers = findViewById(R.id.viewersrecyler);

        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        txtsendgift = findViewById(R.id.txtsendgift);
        txtusercoin = findViewById(R.id.txtusercoin);
        spinner = findViewById(R.id.spinner);
        crystal = findViewById(R.id.giftslayout);
        closegift = findViewById(R.id.closegift);
        btngift = findViewById(R.id.btngift);
        txtcmnt = findViewById(R.id.txtcmnt);
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
        ivpigions=findViewById(R.id.pigion);
        ivoscar=findViewById(R.id.oscar);
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

        enterAnimation = AnimationUtils.loadAnimation(this, R.anim.enter);
        leaveAnimation = AnimationUtils.loadAnimation(this, R.anim.exit);
        //giftAnimation = findViewById(R.id.giftAnimation);

        txtviewerscount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowUser();
            }
        });
        closegift.setOnClickListener(this);
        btngift.setOnClickListener(this);
    /*    txtcmnt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){

                }
            }
        });*/
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
        ivpigions.setOnClickListener(this);
        ivoscar.setOnClickListener(this);
        iv1000cake.setOnClickListener(this);
        // iv10kgentwatch.setOnClickListener(this);
        iv10kladiesbag.setOnClickListener(this);
        iv15happy.setOnClickListener(this);
        showusers.setOnClickListener(this);
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
        recyclerView = findViewById(R.id.cmntrecyler);


        String nameofroom = getIntent().getStringExtra("UserName");
        final Comment comment= new Comment();
        if (nameofroom.contentEquals("Board Gamers"))
        {
            comment.setComment("Fellow gamers! Welcome to the world of board games! ");
            comment.setName("Admin - Board Gamers");
            comments.add(comment);
        }
        if (nameofroom.contentEquals("Cat Lovers"))
        {
            comment.setComment("Meow \uD83D\uDC31 ");
            comment.setName("Admin - Cat Lovers");
            comments.add(comment);
        }
        if (nameofroom.contentEquals("Eidland Welcome Hall"))
        {

            comment.setComment("Ramadan Kareem! We are glad to have you here! Please tap on a seat to start speaking");
            comment.setName("Eidland Staff \uD83E\uDD73");
            imgurl = getIntent().getStringExtra("profile");
            comment.setUserphoto(imgurl);
            comments.add(comment);
        }
        commentAdapter = new CommentAdapter(this, comments, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, final int position) {
                if (position == 0) {
                    Toast.makeText(LiveRoomActivity.this, "This is auto Generated Text for every live", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
        setOnlineMembers();
        type = getIntent().getStringExtra("User");

        UserName = getIntent().getStringExtra("UserName");
        imgurl = getIntent().getStringExtra("profile");

        Glide.with(this).load(imgurl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(imgbroad);
        broadname.setText(UserName + " \uD83C\uDF89\uD83C\uDF8A");
       /* Glide.with(this).load(imgurl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(_image0);
        _host_name.setText(UserName);*/
        if (type.equals("Host")) {
            roomname = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgurl,  Staticconfig.user.getEmail(),"host");
            pushid = FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).child(pushid).setValue(viewer);
            AgainSeat = "seat0";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!LiveRoomActivity.this.isDestroyed())
                    Glide.with(LiveRoomActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userimage);
            }
            else {

                Glide.with(LiveRoomActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userimage);

            }
            viewerslist.clear();
            setOnlineMembers();
            gettoken(true);
        } else {
            roomname = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = getIntent().getStringExtra("userid");

            Viewer comment1 = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(), Staticconfig.user.getEmail(),Staticconfig.user.getName());
            SelfUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            pushid = FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).child(pushid).setValue(comment1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if(!LiveRoomActivity.this.isDestroyed())
                    Glide.with(LiveRoomActivity.this).load(Staticconfig.user.getImageurl()).into(userimage);
            }
            else {

                Glide.with(LiveRoomActivity.this).load(Staticconfig.user.getImageurl()).into(userimage);


            }

            gettoken(false);

        }

        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialoge = new AlertDialog.Builder(LiveRoomActivity.this);
            dialoge.setTitle("Confirm")
                    .setMessage("Are you sure you want to leave current session?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (AgainSeat != null) {

                                doSwitchToBroadcaster(false);
                                FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(AgainSeat).removeValue();
                                Uri ir2=FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                                AgainSeat=null;
                                Intent intent = new Intent(LiveRoomActivity.this, MyProfileActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Uri ir3=FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                                Intent intent = new Intent(LiveRoomActivity.this, MyProfileActivity.class);
                                startActivity(intent);
                                finish();

                            }
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
        }




        });

        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).addChildEventListener(eventListener);
        sencmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(txtcmnt.getText().toString())) {
                    Comment comment1 = new Comment(Staticconfig.user.getName(), txtcmnt.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), false, ".", ".",Staticconfig.user.getImageurl());

                    FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomname).push().setValue(comment1);
                    txtcmnt.setText("");
                }
            }
        });
        selectuseruid = hostuid;

        singlegift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singl.setVisibility(View.GONE);
                crystal.setVisibility(View.VISIBLE);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).child(pushid).onDisconnect().removeValue();
        coincomma=  formattedtext(Staticconfig.user.getCoins());
        txtusercoin.setText(coincomma);
        userref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Staticconfig.user=user;
               // Glide.with(getApplicationContext()).load(Staticconfig.user.getImageurl()).into(userimage);
                //need to check where it set image
                coincomma=  formattedtext(Staticconfig.user.getCoins());
                txtusercoin.setText(coincomma);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setNameAllSeats();
        FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomname).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
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
        giftslistner();
    }
    public void CheckModerator (final String st, String clickeduser, final String seat)
    {
        FirebaseDatabase.getInstance().getReference("Mods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(st)){
                    ModUserRemove.setVisibility(View.VISIBLE);
                    ModUserRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).child(seat).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(seat).removeValue();
                            Toast.makeText(LiveRoomActivity.this,"user removed",Toast.LENGTH_LONG).show();
                            singl.setVisibility(View.INVISIBLE);
                        }
                    });
                }else {
                    ModUserRemove.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String formattedtext (String coin)
    {
        BigDecimal val = new BigDecimal(coin);
        formatter = new DecimalFormat("#,###,###");
        finalText=formatter.format(val);
        return finalText;
    }
    public void inist(String token) {
        event().addEventHandler(this);

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

        worker().joinChannel(token, roomName, config().mUid);

        TextView textRoomName = findViewById(R.id.room_name);
        // textRoomName.setText("CS & IT  Room");

        optional();

       /* LinearLayout bottomContainer = (LinearLayout) findViewById(R.id.bottom_container);
        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();
        fmp.bottomMargin = virtualKeyHeight();*/
    }
    @Override
    protected void initUIandEvent() {
        optional();

       /* LinearLayout bottomContainer = (LinearLayout) findViewById(R.id.bottom_container);
        FrameLayout.MarginLayoutParams fmp = (FrameLayout.MarginLayoutParams) bottomContainer.getLayoutParams();
        fmp.bottomMargin = virtualKeyHeight() + 20;*/
    }
    public void joinChannel(String token) {
        Rooms room = new Rooms(UserName, imgurl, hostuid, token,"0",roomname);
        FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomname).setValue(room);
        SeatsName = "seat1";
        Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(),imgurl,FirebaseAuth.getInstance().getCurrentUser().getEmail(), UserName );
       // ali
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(SeatsName).setValue(viewer);
        AgainSeat=SeatsName;
       // FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(SeatsName).onDisconnect().removeValue();

        inist(token);
    }
    public void gettoken(final boolean ishost) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(LiveRoomActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, "https://auxilium2.herokuapp.com/access_token?channel=" + roomname, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("token");
                    if(ishost)
                    joinChannel(token);
                    else
                        inist(token);

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Log.e("roor", e.getLocalizedMessage() + "d");
                    Toast.makeText(LiveRoomActivity.this, "Json", Toast.LENGTH_SHORT).show();
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
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    Viewer seat0 = snapshot.getValue(Viewer.class);

                   /* Viewer seat1 = snapshot.child("seat1").getValue(Viewer.class);
                    Viewer seat2 = snapshot.child("seat2").getValue(Viewer.class);
                    Viewer seat3 = snapshot.child("seat3").getValue(Viewer.class);
                    Viewer seat4 = snapshot.child("seat4").getValue(Viewer.class);
                    Viewer seat5 = snapshot.child("seat5").getValue(Viewer.class);
                    Viewer seat6 = snapshot.child("seat6").getValue(Viewer.class);
                    Viewer seat7 = snapshot.child("seat7").getValue(Viewer.class);
                    Viewer seat8 = snapshot.child("seat8").getValue(Viewer.class);
                    Viewer seat9 = snapshot.child("seat9").getValue(Viewer.class);*/
                    if (snapshot.getKey().equals("seat0")) {
                        _host_name.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image0);
                    }/* else {
                        _host_name.setText("Seat #0");
                        _image0.setImageResource(R.drawable.adduser);

                    }*/

                    if (snapshot.getKey().equals("seat1")) {
                        _name1.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image1);
                    } /*else {
                        _name1.setText("Seat #1");
                        _image1.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat2")) {
                        _name2.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image2);

                    } /*else {
                        _name2.setText("Seat #2");
                        _image2.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat3")) {
                        _name3.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image3);

                    } /*else {
                        _name3.setText("Seat #3");
                        _image3.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat4")) {
                        _name4.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image4);

                    } /*else {
                        _name4.setText("Seat #4");
                        _image4.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat5")) {
                        _name5.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image5);

                    } /*else {
                        _name5.setText("Seat #5");
                        _image5.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat6")) {
                        _name6.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image6);

                    } /*else {
                        _name6.setText("Seat #6");
                        _image6.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat7")) {
                        _name7.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image7);

                    } /*else {
                        _name7.setText("Seat #7");
                        _image7.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat8")) {
                        _name8.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image8);

                    } /*else {
                        _name8.setText("Seat #8");
                        _image8.setImageResource(R.drawable.adduser);

                    }*/
                    if (snapshot.getKey().equals("seat9")) {
                        _name9.setText(seat0.getName());
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.ic_mic).into(_image9);
                    } /*else {
                        _name9.setText("Seat #9");
                        _image9.setImageResource(R.drawable.adduser);

                    }*/
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Viewer seat00 = snapshot.getValue(Viewer.class);
                    String UserID=seat00.getId();

                    if (snapshot.getKey().equals("seat1")) {
                        _name1.setText("Seat #2");
                        _image1.setImageResource(R.drawable.ic_mic);   }
                    if (snapshot.getKey().equals("seat2")) {
                        _name2.setText("Seat #3");
                        _image2.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat3")) {
                        _name3.setText("Seat #4");
                        _image3.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat4")) {
                        _name4.setText("Seat #5");
                        _image4.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat5")) {
                        _name5.setText("Seat #6");
                        _image5.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat6")) {
                        _name6.setText("Seat #7");
                        _image6.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat7")) {
                        _name7.setText("Seat #8");
                        _image7.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat8")) {
                        _name8.setText("Seat #9");
                        _image8.setImageResource(R.drawable.ic_mic);

                    }
                    if (snapshot.getKey().equals("seat9")) {
                        _name9.setText("Seat #10");
                        _image9.setImageResource(R.drawable.ic_mic);
                    }
                    if (snapshot.getKey().equals("seat0")) {
                        _host_name.setText("Seat #1");
                        _image0.setImageResource(R.drawable.ic_mic);
                    }
                    if(UserID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        doSwitchToBroadcaster(false);
                    }

                  /*  Viewer seat0 = snapshot.child("seat0").getValue(Viewer.class);
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
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.adduser).into(_image0);
                    } else {
                        _host_name.setText("Seat #0");
                        _image0.setImageResource(R.drawable.adduser);

                    }

                    if (seat1 != null) {
                        _name1.setText(seat1.getType());
                        Glide.with(getApplicationContext()).load(seat1.getPhoto()).placeholder(R.drawable.adduser).into(_image1);
                    } else {
                        _name1.setText("Seat #1");
                        _image1.setImageResource(R.drawable.adduser);

                    }
                    if (seat2 != null) {
                        _name2.setText(seat2.getType());
                        Glide.with(getApplicationContext()).load(seat2.getPhoto()).placeholder(R.drawable.adduser).into(_image2);

                    } else {
                        _name2.setText("Seat #2");
                        _image2.setImageResource(R.drawable.adduser);

                    }
                    if (seat3 != null) {
                        _name3.setText(seat3.getType());
                        Glide.with(getApplicationContext()).load(seat3.getPhoto()).placeholder(R.drawable.adduser).into(_image3);

                    } else {
                        _name3.setText("Seat #3");
                        _image3.setImageResource(R.drawable.adduser);

                    }
                    if (seat4 != null) {
                        _name4.setText(seat4.getType());
                        Glide.with(getApplicationContext()).load(seat4.getPhoto()).placeholder(R.drawable.adduser).into(_image4);

                    } else {
                        _name4.setText("Seat #4");
                        _image4.setImageResource(R.drawable.adduser);

                    }
                    if (seat5 != null) {
                        _name5.setText(seat5.getType());
                        Glide.with(getApplicationContext()).load(seat5.getPhoto()).placeholder(R.drawable.adduser).into(_image5);

                    } else {
                        _name5.setText("Seat #5");
                        _image5.setImageResource(R.drawable.adduser);

                    }
                    if (seat6 != null) {
                        _name6.setText(seat6.getType());
                        Glide.with(getApplicationContext()).load(seat6.getPhoto()).placeholder(R.drawable.adduser).into(_image6);

                    } else {
                        _name6.setText("Seat #6");
                        _image6.setImageResource(R.drawable.adduser);

                    }
                    if (seat7 != null) {
                        _name7.setText(seat7.getType());
                        Glide.with(getApplicationContext()).load(seat7.getPhoto()).placeholder(R.drawable.adduser).into(_image7);

                    } else {
                        _name7.setText("Seat #7");
                        _image7.setImageResource(R.drawable.adduser);

                    }
                    if (seat8 != null) {
                        _name8.setText(seat8.getType());
                        Glide.with(getApplicationContext()).load(seat8.getPhoto()).placeholder(R.drawable.adduser).into(_image8);

                    } else {
                        _name8.setText("Seat #8");
                        _image8.setImageResource(R.drawable.adduser);

                    }
                    if (seat9 != null) {
                        _name9.setText(seat9.getType());
                        Glide.with(getApplicationContext()).load(seat9.getPhoto()).placeholder(R.drawable.adduser).into(_image9);
                    } else {
                        _name9.setText("Seat #9");
                        _image9.setImageResource(R.drawable.adduser);

                    }*/
                }
              }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      /*  FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Toast.makeText(LiveRoomActivity.this, "call", Toast.LENGTH_SHORT).show();
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
                        Glide.with(getApplicationContext()).load(seat0.getPhoto()).placeholder(R.drawable.userprofile).into(_image0);
                    } else {
                        _host_name.setText("Seat #0");
                        _image0.setImageResource(R.drawable.userprofile);

                    }

                    if (seat1 != null) {
                        _name1.setText(seat1.getType());
                        Glide.with(getApplicationContext()).load(seat1.getPhoto()).placeholder(R.drawable.userprofile).into(_image1);
                    } else {
                        _name1.setText("Seat #1");
                        _image1.setImageResource(R.drawable.userprofile);

                    }
                    if (seat2 != null) {
                        _name2.setText(seat2.getType());
                        Glide.with(getApplicationContext()).load(seat2.getPhoto()).placeholder(R.drawable.userprofile).into(_image2);

                    } else {
                        _name2.setText("Seat #2");
                        _image2.setImageResource(R.drawable.userprofile);

                    }
                    if (seat3 != null) {
                        _name3.setText(seat3.getType());
                        Glide.with(getApplicationContext()).load(seat3.getPhoto()).placeholder(R.drawable.userprofile).into(_image3);

                    } else {
                        _name3.setText("Seat #3");
                        _image3.setImageResource(R.drawable.userprofile);

                    }
                    if (seat4 != null) {
                        _name4.setText(seat4.getType());
                        Glide.with(getApplicationContext()).load(seat4.getPhoto()).placeholder(R.drawable.userprofile).into(_image4);

                    } else {
                        _name4.setText("Seat #4");
                        _image4.setImageResource(R.drawable.userprofile);

                    }
                    if (seat5 != null) {
                        _name5.setText(seat5.getType());
                        Glide.with(getApplicationContext()).load(seat5.getPhoto()).placeholder(R.drawable.userprofile).into(_image5);

                    } else {
                        _name5.setText("Seat #5");
                        _image5.setImageResource(R.drawable.userprofile);

                    }
                    if (seat6 != null) {
                        _name6.setText(seat6.getType());
                        Glide.with(getApplicationContext()).load(seat6.getPhoto()).placeholder(R.drawable.userprofile).into(_image6);

                    } else {
                        _name6.setText("Seat #6");
                        _image6.setImageResource(R.drawable.userprofile);

                    }
                    if (seat7 != null) {
                        _name7.setText(seat7.getType());
                        Glide.with(getApplicationContext()).load(seat7.getPhoto()).placeholder(R.drawable.userprofile).into(_image7);

                    } else {
                        _name7.setText("Seat #7");
                        _image7.setImageResource(R.drawable.userprofile);

                    }
                    if (seat8 != null) {
                        _name8.setText(seat8.getType());
                        Glide.with(getApplicationContext()).load(seat8.getPhoto()).placeholder(R.drawable.userprofile).into(_image8);

                    } else {
                        _name8.setText("Seat #8");
                        _image8.setImageResource(R.drawable.userprofile);

                    }
                    if (seat9 != null) {
                        _name9.setText(seat9.getType());
                        Glide.with(getApplicationContext()).load(seat9.getPhoto()).placeholder(R.drawable.userprofile).into(_image9);
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
 */   }
    private void toast() {
       // Toast.makeText(this, "You have Already a room", Toast.LENGTH_SHORT).show();
    }
    private void CheckSeats(final String seats) {
        if (AgainSeat == null) {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getId();
                        String checkuid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        CheckModerator(SelfUid,selectuseruid,seats);
                        Glide.with(getApplicationContext()).load(viewer.getPhoto()).into(popup_user);
                        txtsinglename.setText(viewer.getName());
                        singl.setVisibility(View.VISIBLE);
                    } else {

                        boolean checkPermissionResult = checkSelfPermissions();
                        if (checkPermissionResult) {
                            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(),Staticconfig.user.getEmail(), Staticconfig.user.getName());
                            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(seats).setValue(viewer);

                            //  FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(seats).onDisconnect().removeValue();
                            doSwitchToBroadcaster(true);
                            AgainSeat = seats;
                        }





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
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getId();
                        CheckModerator(SelfUid,selectuseruid,seats);
                        Glide.with(getApplicationContext()).load(viewer.getPhoto()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(popup_user);
                        txtsinglename.setText(viewer.getName());
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
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private void optionalDestroy() {
    }

    @Override
    protected void onDestroy() {
       /* if (AgainSeat != null) {
            doSwitchToBroadcaster(false);
            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(AgainSeat).removeValue();

            AgainSeat=null;

        }*/
        super.onDestroy();
    }



    @Override
    protected void onStop() {
      /*  if (AgainSeat != null) {
            doSwitchToBroadcaster(false);
            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(AgainSeat).removeValue();

            AgainSeat=null;

        }*/
        super.onStop();

    }



    public void onSwitchSpeakerClicked(View view) {
        log.info("onSwitchSpeakerClicked " + view + " " + mAudioMuted + " " + mAudioRouting);

        RtcEngine rtcEngine = rtcEngine();
        // Enables/Disables the audio playback route to the speakerphone.
        // This method sets whether the audio is routed to the speakerphone or earpiece. After calling this method, the SDK returns the onAudioRouteChanged callback to indicate the changes.
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3);
    }

    private void doConfigEngine(int cRole) {
        worker().configEngine(cRole);
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();

        doLeaveChannel();
        event().removeEventHandler(this);
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);

    }

    public void onEndCallClicked(View view) {

        if (AgainSeat != null) {
            doSwitchToBroadcaster(false);
            if (flag==true)
            {
                Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);
            }

            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(AgainSeat).removeValue();

            AgainSeat=null;

        }
    }

    @Override
    public void onBackPressed() {
        leave = "dialoge";
        End1();


    }

    private void End1() {
        if (leave.equals("dialoge")) {
            AlertDialog.Builder dialoge = new AlertDialog.Builder(LiveRoomActivity.this);
            dialoge.setTitle("Confirm Exit")
                    .setMessage("Are you sure you want to leave Eidland?")
                    .setPositiveButton("Exit & Leave App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            run = "1";
                            progressDialog.show();
                            EndMeeting();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomname).child(pushid).removeValue();
     //   }

            if (AgainSeat != null) {
                FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(AgainSeat).removeValue();
                log.info("onBackPressed");
                progressDialog.cancel();
                finish();
                LiveRoomActivity.super.onBackPressed();
                run = "1";

            } else {
                progressDialog.cancel();
                finish();
                LiveRoomActivity.super.onBackPressed();
                run = "1";
            }


    }






    private void doSwitchToBroadcaster(boolean broadcaster) {
        final int uid = config().mUid;
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
                 flag = true;
                if (tag != null && (boolean) tag) {
                    flag = false;
                }
                worker().getRtcEngine().muteLocalAudioStream(flag);
                 button = (ImageView) v;
                button.setTag(flag);

                if (flag && muteClicked) {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);


                }
                else if (flag) {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_off).into(button);

                }

                else {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);

                }
            }
        });

        button2.setVisibility(View.VISIBLE);
        bottom_action_end_call.setVisibility(View.VISIBLE);
    }

    public void onVoiceMuteClicked(View view) {
        log.info("onVoiceMuteClicked " + view + " audio_status: " + mAudioMuted);
        muteClicked=true;
        RtcEngine rtcEngine = rtcEngine();
        // Stops/Resumes sending the local audio stream.
        // A successful muteLocalAudioStream method call triggers the onUserMuteAudio callback on the remote client.
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        if (mAudioMuted) {

            Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_off).into(iv);
        } else {
            Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(iv);
        }
    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        String msg = "onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed;
        log.debug(msg);

        notifyMessageChanged(msg);

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
                    showLongToast(getString(R.string.msg_no_network_connection));
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
        leave = "dialoge";
       End1();
    }

    ImageView lastimg;

    public void setselct(ImageView s) {
        Log.v("entered set select", String.valueOf(s));
        lastimg.setImageResource(0);
        lastimg = s;
        s.setImageResource(R.drawable.ic_check_1_gift_select);
        switch (s.getId()) {

            case R.id.iv500hearts:
                selectedgiftname = "hearts";
                break;
            case R.id.iv200redflower:
                selectedgiftname = "smilereact";

                break;
            case R.id.pigion:
                selectedgiftname = "pigions";

                break;
            case R.id.oscar:
                selectedgiftname = "oscar";

                break;
            case R.id.iv1000cake:
                selectedgiftname = "heartcomment";

                break;
            case R.id.iv15khappybirthday:
                selectedgiftname = "like2";

                break;
            case R.id.iv20kgift:
                selectedgiftname = "star";

                break;
            case R.id.iv25kheartcake:
                selectedgiftname = "medal";

                break;

            case R.id.neckless:
                selectedgiftname = "fire";

                break;
            case R.id.dimond:
                selectedgiftname = "debate";

                break;
            case R.id.queen:
                selectedgiftname = "castle";


                break;
            case R.id.king:
                selectedgiftname = "crown";


                break;
            case R.id.earing:
                selectedgiftname = "carousel";


                break;

            case R.id.flowerbuckt:
                selectedgiftname = "championbelt";


                break;
            case R.id.iv10kbag:
                selectedgiftname = "like1";

                break;

            case R.id.handband:
                selectedgiftname = "clap";


                break;
        }
    }

    int selectamnt = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id._seat0:

                CheckSeats("seat0");
                Clickedseat="seat0";
                break;
            case R.id._seat1:
                CheckSeats("seat1");
                Clickedseat="seat1";
                break;
            case R.id._seat2:
                CheckSeats("seat2");
                Clickedseat="seat2";
                break;
            case R.id._seat3:
                CheckSeats("seat3");
                Clickedseat="seat3";
                break;
            case R.id._seat4:
                CheckSeats("seat4");
                Clickedseat="seat4";
                break;
            case R.id._seat5:
                CheckSeats("seat5");
                Clickedseat="seat5";
                break;
            case R.id._seat6:
                CheckSeats("seat6");
                Clickedseat="seat6";
                break;
            case R.id._seat7:
                CheckSeats("seat7");
                Clickedseat="seat7";
                break;
            case R.id._seat8:
                CheckSeats("seat8");
                Clickedseat="seat8";
                break;
            case R.id._seat9:
                CheckSeats("seat9");
                Clickedseat="seat9";
                break;

            case R.id.closegift:
                crystal.setVisibility(View.GONE);
                break;
            case R.id.btngift: //gift icon beside keyboard
                selectuseruid = hostuid;
                txtsinglename.setText(UserName);
                crystal.setVisibility(View.VISIBLE);
                break;

            case R.id.iv500hearts:
                setselct(iv500hearts);
                selectamnt = 30;
                break;
            case R.id.iv200redflower:

                setselct(iv200flower);
                selectamnt = 3;
                break;
            case R.id.oscar:
                setselct(ivoscar);
                selectamnt = 500;
                break;
            case R.id.pigion:

                setselct(ivpigions);
                selectamnt = 99;
                break;
            case R.id.iv1000cake:

                setselct(iv1000cake);

                selectamnt = 15;
                break;
            case R.id.iv15khappybirthday:
                setselct(iv15happy);

                selectamnt = 5;

                break;
            case R.id.iv20kgift:
                setselct(iv20giftpack);

                selectamnt = 50;

                break;
            case R.id.iv25kheartcake:
                setselct(iv25kheartcake);

                selectamnt = 20;
                break;

            case R.id.neckless:
                setselct(iv30kneckless);

                selectamnt = 75;
                break;
            case R.id.dimond:
                setselct(iv40kring);

                selectamnt = 25;
                break;
            case R.id.queen:
                setselct(iv50queen);


                selectamnt = 999;

                break;
            case R.id.king:
                setselct(iv50kking);

                selectamnt = 1000;

                break;

            case R.id.earing:
                setselct(iv50earring);

                selectamnt = 69;


                break;

            case R.id.flowerbuckt:
                setselct(iv50kbucket);

                selectamnt = 100;

                break;
            case R.id.iv10kbag:
                setselct(iv10kladiesbag);

                selectamnt = 1;
                break;

            case R.id.handband:
                setselct(iv25kband);
                selectamnt = 10;

                break;
            case R.id.txtsendgift: //gift layout send button

                if(!selectuseruid.equals(SelfUid)) {
                    if (selectamnt > 0) {
                        Long curnt = Long.parseLong(Staticconfig.user.getCoins());
                        if (curnt > selectamnt) {
                            Log.v("entered 1", String.valueOf(curnt));
                            crystal.setVisibility(View.GONE);
                            userref.child(SelfUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    long l   = Long.parseLong(user.getCoins());
                                    l = l - selectamnt;
                                    user.setCoins(l + "");
                                    userref.child(SelfUid).setValue(user);
                                    Staticconfig.user.setCoins(l + "");
                                    coincomma=  formattedtext(Staticconfig.user.getCoins());
                                    txtusercoin.setText(coincomma);
                                    Log.v("current balance", String.valueOf(coincomma));
                                    Log.v("reciever", selectuseruid);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            userref.child(selectuseruid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    long lr;
                                    if (user.getRecievedCoins() == null) lr = 0;
                                    else lr   = Long.parseLong(user.getRecievedCoins());
                                    lr = lr + selectamnt;
                                    user.setRecievedCoins(lr + "");
                                    userref.child(selectuseruid).setValue(user);
                                    Staticconfig.user.setRecievedCoins(lr + "");
                                    coincomma=  formattedtext(Staticconfig.user.getRecievedCoins());
                                    txtusercoin.setText(coincomma);
                                    Log.v("current recieved", String.valueOf(coincomma));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            sendgift();
                        } else
                        {
                           if (selectamnt==0)
                           {
                               Toast.makeText(this, "No Gift is selected", Toast.LENGTH_SHORT).show();
                           }
                           else Toast.makeText(this, "Low Balance Please Purchase Coins", Toast.LENGTH_SHORT).show();
                        }

                    } else
                        Toast.makeText(this, "No Gift is selected", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "You can not send gift to yourself", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txtusercoin:
                startActivity(new Intent(LiveRoomActivity.this, WalletActivity.class));

                break;


        }

    }

    String selectedgiftname = "flower";
    boolean isnotfirst=false;
    public void giftslistner(){
        addpoints();
        FirebaseDatabase.getInstance().getReference().child("gifts").child(roomname).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {
                    if(isnotfirst) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Gift gift = dataSnapshot1.getValue(Gift.class);
                            if (gift.getGift() != null && gift.getSender() != null) {
                                giftslist.add(new Gift(gift.getGift(), gift.getSender(), gift.getSendername(),gift.getReceivername()));
                                Log.e("gift list", gift.getSender() + gift.getGift());
                            }
                        }
//                        if (animatedlayout.getVisibility() == View.VISIBLE && giftslist.size() > 0) {
//                            //  addcomnt(giftslist.get(0).getPhoto(),giftslist.get(0).getId());
//                            giftsend(giftslist.get(0).getGift(), giftslist.get(0).getSender(), giftslist.get(0).getSendername(),giftslist.get(0).getReceivername());
//                        }
                    }
                    else
                        isnotfirst=true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void showGiftAnimationLeftTorRight(String id, Gift gift){
        switch (id){
            case "hearts":
                simplegift.setImageResource(R.drawable.ic_heart);
                //   Constants.user.setDimond(Constants.user.getDimond()-500);
                break;
            case "like1":
                simplegift.setImageResource(R.drawable.ic_like_1);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "smilereact":
                simplegift.setImageResource(R.drawable.ic_heart_1_);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "pigions":
                simplegift.setImageResource(R.drawable.ic_pigeon);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "oscar":
                simplegift.setImageResource(R.drawable.ic_oscar);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "heartcomment":
                simplegift.setImageResource(R.drawable.ic_heartcomment);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;

            case "like2":
                simplegift.setImageResource(R.drawable.ic_like);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "star":
                simplegift.setImageResource(R.drawable.ic_star);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "medal":
                simplegift.setImageResource(R.drawable.ic_medal);
                confetti.setImageResource(R.drawable.confetti);
                confettiLayout.setVisibility(View.VISIBLE);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "fire":
                simplegift.setImageResource(R.drawable.ic_fire);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "debate":
                simplegift.setImageResource(R.drawable.ic_debate);
                // talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "castle":
                simplegift.setImageResource(R.drawable.ic_sand_castle);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "crown":
                simplegift.setImageResource(R.drawable.ic_crown);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "carousel":
                simplegift.setImageResource(R.drawable.ic_carousel);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "championbelt":
                simplegift.setImageResource(R.drawable.ic_champion_belt);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
            case "clap":
                simplegift.setImageResource(R.drawable.ic_clapping);
                //  talntcoins.setText(Constants.user.getDimond()+"");
                break;
        }
        Handler stayOnScreen = new Handler();
        stayOnScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= LOLLIPOP) {
                    animatedlayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter);
                    animatedlayout.startAnimation(animation);
                }
            }
        }, 1000);
        Handler exitScreen = new Handler();
        exitScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateCurveMotion();
                animatedlayout.setVisibility(View.GONE);
                confettiLayout.setVisibility(View.GONE);
                giftslist.remove(0);
                if(giftslist.size()>0){
                    giftsend(giftslist.get(0).getGift(),giftslist.get(0).getSender(),giftslist.get(0).getSendername(),giftslist.get(0).getReceivername());
                }
            }
        }, 2500);

    }
    public void giftsend(final String gift, final String sender,String senderName,String receiver){
     /*   databaseReference.child("Viewers").child(broadid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Viewer viewer=dsp.getValue(Viewer.class);
                    if(viewer.getId().equals(firebaseUser.getUid())) {*/

        sendername.setText(senderName + " contributed to");
        receivername.setText(receiver);
     //  Glide.with(getApplicationContext()).load(sender).error(R.drawable.appicon).into(senderimg);
        //showlocalgift(gift);

        // }

        //  }
        //  }

         /*   @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
    private void sendgift() {
    Gift gift = new Gift();
    gift.setGift(selectedgiftname);
    gift.setReceivername(txtsinglename.getText().toString());
    gift.setSendername(Staticconfig.user.getName());
    gift.setSender(Staticconfig.user.getImageurl());

    FirebaseDatabase.getInstance().getReference().child("gifts").child(roomname).push().setValue(gift);
    Comment comment = new Comment(Staticconfig.user.getName(), "Contributed to " + txtsinglename.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), true, selectedgiftname, "1",Staticconfig.user.getImageurl());
    FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomname).push().setValue(comment);

    showGiftAnimationLeftTorRight(gift.getGift(), gift);
    }
    ArrayList<Point> path;
    public void addpoints() {

        path = new ArrayList<>();
        int y = 100;

            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(-1, height));
            path.add(new Point(-2, height));
            path.add(new Point(width+200 , height));

    }

    private void animateCurveMotion() {
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit);
        animatedlayout.startAnimation(animation2);
//        Path path = new Path();
//        for (int i = 1; i < this.path.size(); i++) {
//            path.moveTo(this.path.get(0).x, this.path.get(0).y);
//            path.lineTo(this.path.get(i).x, this.path.get(i).y);
//        }
//
//        ObjectAnimator objectAnimator = null;
//        if (android.os.Build.VERSION.SDK_INT >= LOLLIPOP) {
//            objectAnimator = ObjectAnimator.ofFloat(animatedlayout, View.X, View.Y, path);
//        }
//        setAnimValues(objectAnimator, 2000, ValueAnimator.RESTART);
//        objectAnimator.start();
    }
    public void setAnimValues(ObjectAnimator objectAnimator, int duration, int repeatMode) {
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(0);
        objectAnimator.setRepeatMode(repeatMode);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        if(Staticconfig.user!=null) {

            Glide.with(LiveRoomActivity.this).load(Staticconfig.user.getImageurl()).into(userimage);
            //setOnlineMembers();


        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }
    public void setOnlineMembers()
    {
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
                    log.error(String.valueOf(i), viewer.getPhoto());
                    if (viewerslist.get(i).getId().equals(viewer.getId())) {
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
*/ FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomname).child("viewers").setValue(viewerslist.size()+"");
                    viewerAdapter.notifyDataSetChanged();
                    txtviewerscount.setText(viewerslist.size()+" Online");
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
                    if (viewerslist.get(i).getId().equals(dataSnapshot.getValue(Viewer.class).getId())) {
                        viewerslist.remove(i);
                        break;
                    }

                }
                FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomname).child("viewers").setValue(viewerslist.size()+"");

                // talentviewers.setText(viewerslist.size()+"");
                viewerAdapter.notifyDataSetChanged();
                txtviewerscount.setText(viewerslist.size()+" Online");

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
        viewerAdapter = new ViewerAdapter(LiveRoomActivity.this, viewerslist, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, int position) {
            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        viewers.setAdapter(viewerAdapter);

    }
    private boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO) &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
    }
    public boolean checkSelfPermission(String permission, int requestCode) {
        //   log.debug("checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }

        if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
            ((AGApplication) getApplication()).initWorkerThread();
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Log.e("onRequestPermissionsResult " + requestCode + " " + Arrays.toString(permissions) + " " + Arrays.toString(grantResults));
        switch (requestCode) {
            case ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    ((AGApplication) getApplication()).initWorkerThread();
                    Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(),Staticconfig.user.getEmail(), Staticconfig.user.getName());
                    FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomname).child(Clickedseat).setValue(viewer);

                    //  FirebaseDatabase.getInstance().getReference().child("Audiance").child(hostuid).child(seats).onDisconnect().removeValue();
                    doSwitchToBroadcaster(true);
                    AgainSeat = Clickedseat;

                } else {
                    Callalert();
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish();
                }
                break;
            }
        }
    }
    public void Callalert ()
    {
        AlertDialog.Builder dialoge = new AlertDialog.Builder(LiveRoomActivity.this);
        dialoge.setTitle("Welcome to Eidland")
                .setMessage("Please ensure your microphone and storage permission is given in order to get most of Eidland")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean checkPermissionResult = checkSelfPermissions();
                        if (checkPermissionResult)
                        {

                            dialog.cancel();
                        }
                        else Log.e("no permission", "Not Found");
                    }
                })

                .setCancelable(false)
                .show();
    }

    public void ShowUser() {

        ViewDialoguser viewDialoguser=new ViewDialoguser(this);
        viewDialoguser.showDialog(viewerslist);
    }
}
