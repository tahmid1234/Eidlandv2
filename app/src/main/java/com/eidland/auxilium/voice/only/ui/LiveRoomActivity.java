package com.eidland.auxilium.voice.only.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import com.eidland.auxilium.voice.only.model.AGEventHandler;
import com.eidland.auxilium.voice.only.model.AnimationItem;
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

import static com.eidland.auxilium.voice.only.ui.Helper.getFormattedText;

public class LiveRoomActivity extends BaseActivity implements AGEventHandler, AdapterSeat.OnSeatClickListener, AdapterGift.OnGiftClickListener {
    String type, SeatsName, AgainSeat, run;
    TextView onlineUserCount, broadName, sendGiftBtn, userAvailableCoin;
    ImageView sencmnt;
    ProgressDialog progressDialog;
    String selectedGiftName = "flowers";
    TextView ModUserRemove;
    Boolean muteClicked = false;
    ImageView button2, imgbroad;
    ImageView button1;
    ImageView leaveRoom;
    ArrayList<Viewer> onlineUserList = new ArrayList<>();
    String hostuid, roomName;
    Spinner spinner;
    String selectuseruid;
    EditText commentBox;
    String Clickedseat = null;
    private final static Logger log = LoggerFactory.getLogger(LiveRoomActivity.class);
    CircleImageView popup_user;
    TextView popup_uname;
    private volatile boolean mAudioMuted = false;
    ImageView userImage;
    private volatile int mAudioRouting = -1; // Default
    ChildEventListener eventListener;
    String imgUrl;
    RecyclerView viewers;
    ImageView bottom_action_end_call;
    ArrayList<Comment> comments;
    CommentAdapter commentAdapter;
    ImageView roomGift, closeGiftBox, singleUserClose, singleimg;
    LinearLayout crystal;
    TextView txtsinglename, txtsinglegiftsend, sendername, receivername;
    RelativeLayout singlegift;
    DatabaseReference userRef;
    FirebaseUser currentUser;
    Viewer selectedViewer = new Viewer();
    RelativeLayout singleUserBox;
    ImageView button;

    RelativeLayout animatedLayout;
    RelativeLayout backgroundGIFLayout;
    GifImageView backgrundGIF;
    GifImageView simpleGift;
    boolean flag;
    ArrayList<Gift> giftList, leaderGiftList;

    String nameOfRoom;

    ImageView lastImg;
    int selectedGiftAmount = 0;
    boolean isnotfirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Your Room is being ready..");
        progressDialog.setCancelable(false);

        leaveRoom = findViewById(R.id._leave);
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
        backgrundGIF = findViewById(R.id.backgrundGIF);
        backgroundGIFLayout = findViewById(R.id.backgroundGIFLayout);
        giftList = new ArrayList<>();
        leaderGiftList = new ArrayList<>();
        singleUserBox = findViewById(R.id.single_user_box);
        viewers = findViewById(R.id.viewersrecyler);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        sendGiftBtn = findViewById(R.id.send_gift);
        userAvailableCoin = findViewById(R.id.user_available_coin);
        spinner = findViewById(R.id.spinner);
        crystal = findViewById(R.id.giftslayout);
        closeGiftBox = findViewById(R.id.closegift);
        roomGift = findViewById(R.id.room_gift);
        commentBox = findViewById(R.id.comment_box);
        sencmnt = findViewById(R.id.sndcmnt);
        imgbroad = findViewById(R.id.hostimg);
        broadName = findViewById(R.id.room_name);


        onlineUserCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialogUser viewDialoguser = new ViewDialogUser(LiveRoomActivity.this, width, height);
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

        leaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                End1();
            }
        });
        comments = new ArrayList<>();
        RecyclerView commentRecyclerView = findViewById(R.id.live_comment_recyler);
        commentAdapter = new CommentAdapter(this, comments, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, final int position) {
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


        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        type = getIntent().getStringExtra("User");

        imgUrl = getIntent().getStringExtra("profile");

        Glide.with(this).load(imgUrl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(imgbroad);
        broadName.setText(nameOfRoom + " \uD83E\uDD4A\uD83C\uDFC6\uD83C\uDFC5");

        if (type.equals("Host")) {
            roomName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgUrl, Staticconfig.user.getEmail(), "host");
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(viewer);
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
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(comment1);
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
                                try{

                                    FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }catch (Exception e){

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

        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).onDisconnect().removeValue();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    User user = snapshot.getValue(User.class);
                    Staticconfig.user = user;
                    userAvailableCoin.setText(getFormattedText(Staticconfig.user.getCoins()));
                    System.out.println("okok");
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
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
        GridLayoutManager seatLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 5, GridLayoutManager.VERTICAL, false);
        AdapterSeat adapterSeat = new AdapterSeat(LiveRoomActivity.this, this, roomName);
        seatRecycler.setLayoutManager(seatLayoutManager);
        adapterSeat.notifyDataSetChanged();
        seatRecycler.setAdapter(adapterSeat);

        RecyclerView giftRecycler = findViewById(R.id.gift_recycler);
        giftRecycler.setHasFixedSize(true);
        GridLayoutManager giftLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 2, GridLayoutManager.HORIZONTAL, false);
        AdapterGift adapterGift = new AdapterGift(LiveRoomActivity.this, this, width/4);
        giftRecycler.setLayoutManager(giftLayoutManager);
        adapterGift.notifyDataSetChanged();
        giftRecycler.setAdapter(adapterGift);


        setOnlineMembers();

        sendGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                                    try{
                                        User user = currentData.getValue(User.class);
                                        user.coins = String.valueOf(Long.parseLong(user.coins) - selectedGiftAmount);
                                        currentData.setValue(user);
                                    }catch (Exception e){
                                        System.out.println(e);
                                    }
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                    try{
                                        User user = currentData.getValue(User.class);
                                        Staticconfig.user = user;
                                        userAvailableCoin.setText(getFormattedText(user.coins));
                                    }catch (Exception e){
                                        System.out.println(e);
                                    }
                                }
                            });
                            FirebaseDatabase.getInstance().getReference().child("Users").child(selectuseruid).runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                    try {
                                        User user = currentData.getValue(User.class);
                                        assert user != null;
                                        user.receivedCoins = String.valueOf(Long.parseLong(user.receivedCoins) + selectedGiftAmount);
                                        currentData.setValue(user);
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
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
                                Toast.makeText(getApplicationContext(), "No Gift is selected", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(getApplicationContext(), "Low Balance Please Purchase Coins", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "No Gift is selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You can not send gift to yourself", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSeatClick(int position) {
        CheckSeats("seat" + position);
        Clickedseat = "seat" + position;
    }

    @Override
    public void onGiftClick(int position, ImageView icon) {
        GiftItem giftItem = ConstantApp.giftList().get(position);
        try {
            if (lastImg != null) lastImg.setImageResource(0);
            lastImg = icon;
        } catch (Exception e) {
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

    public void giftsListner() {
        FirebaseDatabase.getInstance().getReference().child("gifts").child(roomName).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isnotfirst) {
                    giftList.clear();
                    leaderGiftList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Gift gift = dataSnapshot1.getValue(Gift.class);

                        assert gift != null;
                        if (gift.getGift() != null && gift.getSenderName() != null) {
                            giftList.add(gift);
                        }
                        if (gift.getGift() != null && gift.getSenderName() != null) {
                            leaderGiftList.add(gift);
                        }
                    }

                    int index = giftList.size() - 1;
                    giftAnimation(giftList.get(index).getGift(), giftList.get(index), giftList.get(index).getReceiverName());

                    LeaderBoard leaderBoard = new LeaderBoard(leaderGiftList, hostuid);

                    RecyclerView topSpeakerRecycler = findViewById(R.id.top_speaker);
                    topSpeakerRecycler.setHasFixedSize(true);
                    LinearLayoutManager giftLayoutManager = new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    AdapterLeadUser adapterLeader = new AdapterLeadUser(LiveRoomActivity.this, leaderBoard.getTopSpeaker());
                    topSpeakerRecycler.setLayoutManager(giftLayoutManager);
                    adapterLeader.notifyDataSetChanged();
                    topSpeakerRecycler.setAdapter(adapterLeader);

                    RecyclerView topContributorRecycler = findViewById(R.id.top_contributor);
                    topContributorRecycler.setHasFixedSize(true);
                    LinearLayoutManager contributorLayoutManager = new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    AdapterLeadUser adapterLeaderContributor = new AdapterLeadUser(LiveRoomActivity.this, leaderBoard.getTopContributor());
                    topContributorRecycler.setLayoutManager(contributorLayoutManager);
                    adapterLeader.notifyDataSetChanged();
                    topContributorRecycler.setAdapter(adapterLeaderContributor);

                } else
                    isnotfirst = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void giftAnimation(String id, Gift gift, String receiver) {


        for (AnimationItem animationItem :
                ConstantApp.animationItems()) {
            if (animationItem.name.equals(id)) {
                simpleGift.setImageResource(animationItem.giftIconId);
                backgrundGIF.setImageResource(animationItem.gifIconId);
                backgroundGIFLayout.setVisibility(View.VISIBLE);
            }
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
                animatedLayout.setVisibility(View.INVISIBLE);
                backgroundGIFLayout.setVisibility(View.GONE);
                try {
                    giftList.remove(0);
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
    }

    public void setOnlineMembers() {


        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onlineUserList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        try {
                            Viewer viewer = data.getValue(Viewer.class);
                            onlineUserList.add(viewer);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }

                RecyclerView viewers = findViewById(R.id.viewersrecyler);
                viewers.hasFixedSize();
                viewers.setLayoutManager(new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, true));
                ViewerAdapter viewerAdapter = new ViewerAdapter(LiveRoomActivity.this, onlineUserList);
                viewers.setAdapter(viewerAdapter);
                viewerAdapter.notifyDataSetChanged();
                onlineUserCount.setText(onlineUserList.size() + " Online");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        eventListener = new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).addChildEventListener(eventListener);
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

    @Override
    protected void deInitUIandEvent() {
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
        End1();
    }

    private void End1() {

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
    }


    private void EndMeeting() {
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
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
        mAudioRouting = routing;
        ImageView iv = findViewById(R.id.switch_speaker_id);
        if (mAudioRouting == 3) { // Speakerphone
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
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

    private boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO) &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
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
}
