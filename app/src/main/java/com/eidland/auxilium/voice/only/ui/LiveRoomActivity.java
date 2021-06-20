package com.eidland.auxilium.voice.only.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.media.AudioManager;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.eidland.auxilium.voice.only.model.GiftItem;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
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

public class LiveRoomActivity extends BaseActivity implements AGEventHandler, View.OnClickListener, AdapterSeat.OnSeatClickListener, AdapterGift.OnGiftClickListener {
    String Seats, type, UserName, SeatsName, AgainSeat, leave = null, run;
    TextView onlineUserCount, broadName, SendGift, userAvailableCoin;
    ImageView sencmnt;
    ProgressDialog progressDialog;
    String selectedGiftName = "flowers";
    TextView ModUserRemove;
    Boolean muteClicked = false;
    ImageView button2, imgbroad;
    ImageView button1;
    String pushid = "";
    DecimalFormat formatter;
    String finalText, coinWithComma;
    ArrayList<Viewer> onlineUserList;
    ViewerAdapter viewerAdapter;
    String hostuid, roomName;
    Spinner spinner;
    String selectuseruid;
    EditText commentBox;
    String Clickedseat = null;
    private final static Logger log = LoggerFactory.getLogger(LiveRoomActivity.class);
    CircleImageView popup_user, commentuser;
    TextView popup_uname;
    private volatile boolean mAudioMuted = false;
    ImageView userImage;
    private volatile int mAudioRouting = -1; // Default
    ChildEventListener eventListener;
    String imgUrl;
    RecyclerView viewers, viewerlist;
    ImageView bottom_action_end_call;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
    ViewerListAdapter viewerListAdapter;
    ImageView roomGift, closeGiftBox, singleUserClose, singleimg, senderimg;
    LinearLayout crystal;
    TextView txtsinglename, txtsinglegiftsend, sendername, receivername;
    RelativeLayout singlegift;
    DatabaseReference userRef;
    FirebaseUser currentUser;
    Viewer selectedViewer = new Viewer();
    RelativeLayout singleUserBox;
    ImageView button;
    LinearLayout contentView;

    RelativeLayout animatedLayout;
    RelativeLayout confettiLayout;
    GifImageView confetti;
    GifImageView simpleGift;
    boolean flag;
    ArrayList<Gift> giftslist, leaderGiftList;
    int height, width;

    String nameOfRoom;


    LinearLayout speaker1, speaker2, speaker3;
    ImageView speaker1Img, speaker2Img, speaker3Img;
    TextView speaker1Coin, speaker2Coin, speaker3Coin;

    LinearLayout supporter1, supporter2, supporter3;
    ImageView supporter1Img, supporter2Img, supporter3Img;
    TextView supporter1Coin, supporter2Coin, supporter3Coin;

    private GridLayoutManager seatLayoutManager, giftLayoutManager;
    private AdapterSeat adapterSeat;
    private AdapterGift adapterGift;

    ImageView lastImg;
    int selectedGiftAmount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels / 2;
        height = height - 150;

        width = displayMetrics.widthPixels;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Your Room is being ready..");
        progressDialog.setCancelable(false);

        userImage = findViewById(R.id._userchatroom);
        button2 = (ImageView) findViewById(R.id.mute_local_speaker_id);
        button1 = (ImageView) findViewById(R.id.switch_broadcasting_id);
        ModUserRemove = findViewById(R.id.removeUser);
        bottom_action_end_call = (ImageView) findViewById(R.id.bottom_action_end_call);
        onlineUserCount = findViewById(R.id.online_user_count);
        txtsinglegiftsend = findViewById(R.id.singlegiftsend);
        singlegift = findViewById(R.id.singlesendgift);
        txtsinglename = findViewById(R.id.txtnamepopup);
        singleimg = findViewById(R.id.singleimg);
        singleUserClose = findViewById(R.id.close);
        simpleGift = findViewById(R.id.imggif);
        sendername = findViewById(R.id.sendername);
        receivername = findViewById(R.id.receivername);
        popup_uname = findViewById(R.id.txtnamepopup);
        popup_user = findViewById(R.id.userimgpopup);
        animatedLayout = findViewById(R.id.animatedlayout);
        confetti = findViewById(R.id.confetti);
        confettiLayout = findViewById(R.id.confettiLayout);
        giftslist = new ArrayList<>();
        leaderGiftList = new ArrayList<>();
        singleUserBox = findViewById(R.id.single_user_box);
        viewers = findViewById(R.id.viewersrecyler);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        SendGift = findViewById(R.id.send_gift);
        userAvailableCoin = findViewById(R.id.user_available_coin);
        spinner = findViewById(R.id.spinner);
        crystal = findViewById(R.id.giftslayout);
        closeGiftBox = findViewById(R.id.closegift);
        roomGift = findViewById(R.id.room_gift);
        commentBox = findViewById(R.id.comment_box);
        sencmnt = findViewById(R.id.sndcmnt);
        imgbroad = findViewById(R.id.hostimg);
        broadName = findViewById(R.id.room_name);


        speaker1 = findViewById(R.id.s1);
        speaker2 = findViewById(R.id.s2);
        speaker3 = findViewById(R.id.s3);

        speaker1Img = findViewById(R.id.s1i);
        speaker2Img = findViewById(R.id.s2i);
        speaker3Img = findViewById(R.id.s3i);

        speaker1Coin = findViewById(R.id.s1t);
        speaker2Coin = findViewById(R.id.s2t);
        speaker3Coin = findViewById(R.id.s3t);

        supporter1 = findViewById(R.id.c1);
        supporter2 = findViewById(R.id.c2);
        supporter3 = findViewById(R.id.c3);

        supporter1Img = findViewById(R.id.c1i);
        supporter2Img = findViewById(R.id.c2i);
        supporter3Img = findViewById(R.id.c3i);

        supporter1Coin = findViewById(R.id.c1t);
        supporter2Coin = findViewById(R.id.c2t);
        supporter3Coin = findViewById(R.id.c3t);


        onlineUserCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogUser viewDialoguser = new ViewDialogUser(LiveRoomActivity.this);
                viewDialoguser.showDialog(onlineUserList);
            }
        });
        closeGiftBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crystal.setVisibility(View.GONE);
            }
        });
        roomGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedViewer.id = "cJupIaBOKXN8QqWzAQMQYFwHzVC3";
                selectedViewer.name = nameOfRoom;
                selectedViewer.photo = "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png";
                selectuseruid = "cJupIaBOKXN8QqWzAQMQYFwHzVC3";
                txtsinglename.setText(nameOfRoom);
                crystal.setVisibility(View.VISIBLE);
            }
        });
        singleUserClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleUserBox.setVisibility(View.GONE);
            }
        });

        userAvailableCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LiveRoomActivity.this, WalletActivity.class));
            }
        });



        comments = new ArrayList<>();
        RecyclerView commentRecyclerView = findViewById(R.id.live_comment_recyler);
        commentAdapter = new CommentAdapter(this, comments, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, final int position) {
                if (position == 0) {
                }

            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        commentRecyclerView.hasFixedSize();
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);



        nameOfRoom = getIntent().getStringExtra("UserName");
        final Comment comment = new Comment();
        if (nameOfRoom.contentEquals("Board Gamers")) {
            comment.setComment("Fellow gamers! Welcome to the world of board games! ");
            comment.setName("Admin - Board Gamers");
            comments.add(comment);
        }
        if (nameOfRoom.contentEquals("Cat Lovers")) {
            comment.setComment("Meow \uD83D\uDC31 ");
            comment.setName("Admin - Cat Lovers");
            comments.add(comment);
        }
        if (nameOfRoom.contentEquals("Eidland Battle Royale")) {
            comment.setComment("Welcome to Eidland! We are glad to have you here! Please tap on a seat to start speaking");
            comment.setName("Eidland Staff \uD83E\uDD73");
            imgUrl = getIntent().getStringExtra("profile");
            comment.setUserphoto(imgUrl);
            comments.add(comment);
        }



        SendGift.setOnClickListener(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setOnlineMembers();
        type = getIntent().getStringExtra("User");

        imgUrl = getIntent().getStringExtra("profile");

        Glide.with(this).load(imgUrl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(imgbroad);
        broadName.setText(nameOfRoom + " \uD83E\uDD4A\uD83C\uDFC6\uD83C\uDFC5");

        if (type.equals("Host")) {
            roomName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgUrl, Staticconfig.user.getEmail(), "host");
            pushid = FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(pushid).setValue(viewer);
            AgainSeat = "seat0";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!LiveRoomActivity.this.isDestroyed())
                    Glide.with(LiveRoomActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImage);
            } else {

                Glide.with(LiveRoomActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImage);

            }
            onlineUserList.clear();
            setOnlineMembers();
            getToken(true);
        } else {
            roomName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = getIntent().getStringExtra("userid");

            Viewer comment1 = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(), Staticconfig.user.getEmail(), Staticconfig.user.getName());
            pushid = FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).push().getKey();
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(pushid).setValue(comment1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!LiveRoomActivity.this.isDestroyed())
                    Glide.with(LiveRoomActivity.this).load(Staticconfig.user.getImageurl()).into(userImage);
            } else {
                Glide.with(LiveRoomActivity.this).load(Staticconfig.user.getImageurl()).into(userImage);
            }

            getToken(false);

        }

        userImage.setOnClickListener(new View.OnClickListener() {
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
                                    FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();

                                    AgainSeat = null;
                                    Intent intent = new Intent(LiveRoomActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

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

        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).addChildEventListener(eventListener);
        sencmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(commentBox.getText().toString())) {
                    Comment comment1 = new Comment(Staticconfig.user.getName(), commentBox.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), false, ".", ".", Staticconfig.user.getImageurl());

                    FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomName).push().setValue(comment1);
                    commentBox.setText("");
                }
            }
        });
        selectuseruid = hostuid;

        singlegift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleUserBox.setVisibility(View.GONE);
                crystal.setVisibility(View.VISIBLE);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(pushid).onDisconnect().removeValue();
        coinWithComma = getFormattedText(Staticconfig.user.getCoins());
        userAvailableCoin.setText(coinWithComma);
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Staticconfig.user = user;
                coinWithComma = getFormattedText(Staticconfig.user.getCoins());
                userAvailableCoin.setText(coinWithComma);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setNameAllSeats();

        giftsListner();



        FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomName).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childd : dataSnapshot.getChildren()) {
                    comments.add(childd.getValue(Comment.class));
                }
                commentAdapter.notifyDataSetChanged();
                commentRecyclerView.smoothScrollToPosition(comments.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RecyclerView seatRecycler = findViewById(R.id.seat_recycler);
        seatRecycler.setHasFixedSize(true);
        seatLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 5, GridLayoutManager.VERTICAL, false);
        adapterSeat = new AdapterSeat(LiveRoomActivity.this, this, roomName);
        seatRecycler.setLayoutManager(seatLayoutManager);
        adapterSeat.notifyDataSetChanged();
        seatRecycler.setAdapter(adapterSeat);


        RecyclerView giftRecycler = findViewById(R.id.gift_recycler);
        giftRecycler.setHasFixedSize(true);
        giftLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 2, GridLayoutManager.HORIZONTAL, false);
        adapterGift = new AdapterGift(LiveRoomActivity.this, this, roomName);
        giftRecycler.setLayoutManager(giftLayoutManager);
        adapterGift.notifyDataSetChanged();
        giftRecycler.setAdapter(adapterGift);
    }

    @Override
    public void onSeatClick(int position) {
        CheckSeats("seat"+ position);
        Clickedseat = "seat"+ position;
    }

    @Override
    public void onGiftClick(int position, ImageView icon) {
        GiftItem giftItem = ConstantApp.giftList().get(position);
        try{
            if(lastImg!=null) lastImg.setImageResource(0);
            lastImg = icon;
        }catch (Exception e){
            System.out.println(e);
        }
        icon.setImageResource(R.drawable.ic_check_1_gift_select);
        selectedGiftName = giftItem.name;
        selectedGiftAmount = giftItem.amount;
    }

    public void CheckModerator(final String st, String clickeduser, final String seat) {
        FirebaseDatabase.getInstance().getReference("Mods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(st)) {
                    ModUserRemove.setVisibility(View.VISIBLE);
                    ModUserRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seat).removeValue();
                            Toast.makeText(LiveRoomActivity.this, "user removed", Toast.LENGTH_LONG).show();
                            singleUserBox.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    ModUserRemove.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getFormattedText(String coin) {
        BigDecimal val = new BigDecimal(coin);
        formatter = new DecimalFormat("#,###,###");
        finalText = formatter.format(val);
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
    }

    @Override
    protected void initUIandEvent() {
        optional();
    }

    public void joinChannel(String token) {
        Rooms room = new Rooms(nameOfRoom, imgUrl, hostuid, token, "0", roomName);
        FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomName).setValue(room);
        SeatsName = "seat1";
        Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgUrl, FirebaseAuth.getInstance().getCurrentUser().getEmail(), nameOfRoom);
        // ali
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(SeatsName).setValue(viewer);
        AgainSeat = SeatsName;
        inist(token);
    }

    public void getToken(final boolean isHost) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(LiveRoomActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, "https://auxilium2.herokuapp.com/access_token?channel=" + roomName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("token");
                    if (isHost)
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
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Viewer seat00 = snapshot.getValue(Viewer.class);
                    String UserID = seat00.getUid();
                    if (UserID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        doSwitchToBroadcaster(false);
                        System.out.println("doSwitchToBroadcaster");
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckSeats(final String seats) {
        if (AgainSeat == null) {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getUid();

                        selectedViewer = viewer;
                        CheckModerator(currentUser.getUid(), selectuseruid, seats);
                        Glide.with(getApplicationContext()).load(viewer.getPhotoUrl()).into(popup_user);
                        txtsinglename.setText(viewer.getName());
                        singleUserBox.setVisibility(View.VISIBLE);
                    } else {

                        boolean checkPermissionResult = checkSelfPermissions();
                        if (checkPermissionResult) {
                            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(), Staticconfig.user.getEmail(), Staticconfig.user.getName());
                            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats).setValue(viewer);
                            doSwitchToBroadcaster(true);
                            AgainSeat = seats;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getUid();

                        selectedViewer = viewer;
                        CheckModerator(currentUser.getUid(), selectuseruid, seats);
                        Glide.with(getApplicationContext()).load(viewer.getPhotoUrl()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(popup_user);
                        txtsinglename.setText(viewer.getName());
                        singleUserBox.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private void optionalDestroy() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }


    public void onSwitchSpeakerClicked(View view) {
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
            if (flag == true) {
                Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);
            }

            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();

            AgainSeat = null;

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
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(pushid).removeValue();
        if (AgainSeat != null) {
            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();
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


                } else if (flag) {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_off).into(button);

                } else {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);

                }
            }
        });

        button2.setVisibility(View.VISIBLE);
        bottom_action_end_call.setVisibility(View.VISIBLE);
    }

    public void onVoiceMuteClicked(View view) {
        muteClicked = true;
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.send_gift: //gift layout send button
                if (selectuseruid == null)
                    selectuseruid = hostuid;
                if (!selectuseruid.equals(currentUser.getUid())) {
                    if (selectedGiftAmount > 0) {
                        Long curnt = Long.parseLong(Staticconfig.user.getCoins());

                        if (curnt > selectedGiftAmount) {
                            crystal.setVisibility(View.GONE);


                            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                    User user = currentData.getValue(User.class);
                                    user.coins = String.valueOf(Long.parseLong(user.coins) - selectedGiftAmount);
                                    currentData.setValue(user);
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                    User user = currentData.getValue(User.class);
                                    Staticconfig.user = user;
                                    coinWithComma = getFormattedText(user.coins);
                                    userAvailableCoin.setText(coinWithComma);
                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("Users").child(selectuseruid).runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                    User user = currentData.<User>getValue(User.class);
                                    assert user != null;
                                    try {
                                        user.receivedCoins = String.valueOf(Long.parseLong(user.receivedCoins) + selectedGiftAmount);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }

                                    currentData.setValue(user);
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                    System.out.println(error);
                                }
                            });


                            sendGift(new Gift(selectedGiftName, selectedGiftAmount, currentUser.getUid(), Staticconfig.user.name, Staticconfig.user.imageurl, selectuseruid, selectedViewer.getName(), selectedViewer.photo, System.currentTimeMillis()));


                        } else {
                            if (selectedGiftAmount == 0) {
                                Toast.makeText(this, "No Gift is selected", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(this, "Low Balance Please Purchase Coins", Toast.LENGTH_SHORT).show();
                        }

                    } else
                        Toast.makeText(this, "No Gift is selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You can not send gift to yourself", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }


    boolean isnotfirst = true;

    public void giftsListner() {
        addpoints();
        FirebaseDatabase.getInstance().getReference().child("gifts").child(roomName).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isnotfirst) {
                    giftslist.clear();
                    leaderGiftList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Gift gift = dataSnapshot1.getValue(Gift.class);

                        assert gift != null;
                        if (gift.getGift() != null && gift.getSenderName() != null) {
                            giftslist.add(gift);
                        }
                        if (gift.getGift() != null && gift.getSenderName() != null) {
                            leaderGiftList.add(gift);
                        }
                    }

                    Log.d("giftsizeout", String.valueOf(giftslist.size()));
                    int index = giftslist.size() - 1;
                    giftAnimation(giftslist.get(index).getGift(), giftslist.get(index), giftslist.get(index).getReceiverName());


                    LeaderBoard leaderBoard = new LeaderBoard(leaderGiftList, hostuid);// getting wrong hostuid
                    System.out.println(leaderBoard.getTopContributor());
                    System.out.println(leaderBoard.getTopWinner());

                    try {
                        if (leaderBoard.winners.get(0) != null) {
                            speaker1.setVisibility(View.VISIBLE);
                            speaker1Coin.setText(Long.toString(leaderBoard.winners.get(0).coins));
                            Glide.with(getApplicationContext()).load(leaderBoard.winners.get(0).imgUrl).placeholder(R.drawable.ic_mic).into(speaker1Img);
                        }
                        if (leaderBoard.winners.get(1) != null) {
                            speaker2.setVisibility(View.VISIBLE);
                            speaker2Coin.setText(Long.toString(leaderBoard.winners.get(1).coins));
                            Glide.with(getApplicationContext()).load(leaderBoard.winners.get(1).imgUrl).placeholder(R.drawable.ic_mic).into(speaker2Img);
                        }
                        if (leaderBoard.winners.get(2) != null) {
                            speaker3.setVisibility(View.VISIBLE);
                            speaker3Coin.setText(Long.toString(leaderBoard.winners.get(2).coins));
                            Glide.with(getApplicationContext()).load(leaderBoard.winners.get(2).imgUrl).placeholder(R.drawable.ic_mic).into(speaker3Img);
                        }
                    } catch (Exception e) {
                        System.out.println("is working? " + e);
                    }
                    try {
                        if (leaderBoard.contributors.get(0) != null) {
                            supporter1.setVisibility(View.VISIBLE);
                            supporter1Coin.setText(Long.toString(leaderBoard.contributors.get(0).coins));

                            Glide.with(getApplicationContext()).load(leaderBoard.contributors.get(0).imgUrl).placeholder(R.drawable.ic_mic).into(supporter1Img);
                        }
                        if (leaderBoard.contributors.get(1) != null) {
                            supporter2.setVisibility(View.VISIBLE);
                            supporter2Coin.setText(Long.toString(leaderBoard.contributors.get(1).coins));
                            Glide.with(getApplicationContext()).load(leaderBoard.contributors.get(1).imgUrl).placeholder(R.drawable.ic_mic).into(supporter2Img);
                        }
                        if (leaderBoard.contributors.get(2) != null) {
                            supporter3.setVisibility(View.VISIBLE);
                            supporter3Coin.setText(Long.toString(leaderBoard.contributors.get(2).coins));
                            Glide.with(getApplicationContext()).load(leaderBoard.contributors.get(2).imgUrl).placeholder(R.drawable.ic_mic).into(supporter3Img);
                        }
                    } catch (Exception e) {
                        System.out.println("not okay");
                        System.out.println(e);
                    }
                    if (animatedLayout.getVisibility() == View.GONE && giftslist.size() > 0) {
//                            giftsend(giftslist.get(0));
                        System.out.println("okoko");
                    }
                } else
                    isnotfirst = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void giftAnimation(String id, Gift gift, String receiver) {

        switch (id) {
            case "hearts":
                simpleGift.setImageResource(R.drawable.ic_heart);
                break;
            case "like1":
                simpleGift.setImageResource(R.drawable.ic_like_1);
                break;
            case "smilereact":
                simpleGift.setImageResource(R.drawable.ic_heart_1_);
                break;
            case "pigions":
                simpleGift.setImageResource(R.drawable.ic_pigeon);
                break;
            case "oscar":
                simpleGift.setImageResource(R.drawable.ic_oscar);
                break;
            case "heartcomment":
                simpleGift.setImageResource(R.drawable.ic_heartcomment);
                break;

            case "like2":
                simpleGift.setImageResource(R.drawable.ic_like);
                break;
            case "star":
                simpleGift.setImageResource(R.drawable.ic_star);
                break;
            case "medal":
                simpleGift.setImageResource(R.drawable.ic_medal);
                confetti.setImageResource(R.drawable.confetti);
                confettiLayout.setVisibility(View.VISIBLE);
                break;
            case "fire":
                simpleGift.setImageResource(R.drawable.ic_fire);
                break;
            case "debate":
                simpleGift.setImageResource(R.drawable.ic_debate);
                break;

            case "castle":
                simpleGift.setImageResource(R.drawable.ic_sand_castle);
                break;
            case "crown":
                simpleGift.setImageResource(R.drawable.ic_crown);
                break;

            case "carousel":
                simpleGift.setImageResource(R.drawable.ic_carousel);
                break;
            case "championbelt":
                simpleGift.setImageResource(R.drawable.ic_champion_belt);
                break;
            case "clap":
                simpleGift.setImageResource(R.drawable.ic_clapping);
                break;
        }
        sendername.setText(gift.getSenderName() + " Rewarded to ");
        receivername.setText(receiver);
        Handler enterScreen = new Handler();
        enterScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animatedLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter);
                    animatedLayout.setAnimation(animation);
                }
            }
        }, 1500);
        Handler exitScreen = new Handler();
        exitScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit);
                animatedLayout.setAnimation(animation2);
                animatedLayout.setVisibility(View.GONE);
                confettiLayout.setVisibility(View.GONE);
                try {
                    giftslist.remove(0);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, 3000);

    }

    private void sendGift(Gift gift) {

        FirebaseDatabase.getInstance().getReference().child("gifts").child(roomName).push().setValue(gift.toMap());

        Comment comment = new Comment(gift.getSenderName(), "Rewarded to " + txtsinglename.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), true, selectedGiftName, "1", Staticconfig.user.getImageurl());

        FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomName).push().setValue(comment);

        giftAnimation(selectedGiftName, gift, gift.receiverName);
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
        path.add(new Point(width + 200, height));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animateCurveMotion() {

        Path path = new Path();

        path.moveTo(this.path.get(0).x, this.path.get(0).y);

        for (int i = 1; i < this.path.size(); i++) {
            path.lineTo(this.path.get(i).x, this.path.get(i).y);
        }
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(animatedLayout, View.X, View.Y, path);
        setAnimValues(objectAnimator, 2000, ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    public void setAnimValues(ObjectAnimator objectAnimator, int duration, int repeatMode) {
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(0);
        objectAnimator.setRepeatMode(repeatMode);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Staticconfig.user != null) {
            Glide.with(LiveRoomActivity.this).load(Staticconfig.user.getImageurl()).into(userImage);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }

    public void setOnlineMembers() {
        if (viewerlist != null) onlineUserList.clear();
        eventListener = new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Viewer viewer = dataSnapshot.getValue(Viewer.class);

                boolean isexist = false;
                for (int i = 0; i < onlineUserList.size(); i++) {
                    assert viewer != null;
                    log.error(String.valueOf(i), viewer.getPhotoUrl());
                    if (onlineUserList.get(i).getUid().equals(viewer.getUid()) && onlineUserList.get(i).getPhotoUrl().equals(viewer.getPhotoUrl())) {
                        isexist = true;
                        break;
                    } else if (onlineUserList.get(i).getUid().equals(viewer.getUid()) && !onlineUserList.get(i).getPhotoUrl().equals(viewer.getPhotoUrl())) {
                        onlineUserList.remove(i);
                        break;
                    }
                }

                if (!isexist) {
                    onlineUserList.add(viewer);
                    FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomName).child("viewers").setValue(onlineUserList.size() + "");
                    viewerAdapter.notifyDataSetChanged();
                    onlineUserCount.setText(onlineUserList.size() + " Online");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                for (int i = 0; i < onlineUserList.size(); i++) {
                    if (onlineUserList.get(i).getUid().equals(dataSnapshot.getValue(Viewer.class).getUid())) {
                        onlineUserList.remove(i);
                        break;
                    }

                }
                FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomName).child("viewers").setValue(onlineUserList.size() + "");
                viewerAdapter.notifyDataSetChanged();
                onlineUserCount.setText(onlineUserList.size() + " Online");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        onlineUserList = new ArrayList<>();
        viewers.hasFixedSize();
        viewers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        viewerAdapter = new ViewerAdapter(LiveRoomActivity.this, onlineUserList, new ItemClickListener1() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    ((AGApplication) getApplication()).initWorkerThread();
                    Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), Staticconfig.user.getImageurl(), Staticconfig.user.getEmail(), Staticconfig.user.getName());
                    FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(Clickedseat).setValue(viewer);
                    doSwitchToBroadcaster(true);
                    AgainSeat = Clickedseat;

                } else {
                    CallAlert();
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Granted!");
                } else {
                    finish();
                }
                break;
            }
        }
    }

    public void CallAlert() {
        AlertDialog.Builder dialoge = new AlertDialog.Builder(LiveRoomActivity.this);
        dialoge.setTitle("Welcome to Eidland")
                .setMessage("Please ensure your microphone and storage permission is given in order to get most of Eidland")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean checkPermissionResult = checkSelfPermissions();
                        if (checkPermissionResult) {
                            dialog.cancel();
                        } else {
                            Log.e("no permission", "Not Found");
                        }
                    }
                }).setCancelable(false).show();
    }

    public void ShowUser() {
    }

}
