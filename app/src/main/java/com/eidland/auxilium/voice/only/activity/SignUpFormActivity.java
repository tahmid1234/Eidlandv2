package com.eidland.auxilium.voice.only.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.adapter.AdapterAvatar;
import com.eidland.auxilium.voice.only.model.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

import com.eidland.auxilium.voice.only.activity.ViewDialog;

public class SignUpFormActivity<mStorage> extends Activity implements AdapterAvatar.ItemClickListener {
    TextView imgerror;
    RelativeLayout singupactive;
    String finalImage;
    EditText username, email;
    String stringUri, _username, _email;
    CircleImageView imageView;
    AuthCredential credential;
    Uri filePath = Uri.parse("https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit1.jpg?alt=media&token=c420dce9-7ace-42f1-9fa2-9b9450230959");
    String imgpath;
    Intent intent;
    ProgressDialog progressDialog;
    CircleImageView profileimageView;
    ViewDialog viewDialog;
    String gNaame, gEmaail, gImage;
    Boolean ImageUploaded = false, isLoggedin = false;
    FirebaseAuth mAuth;
    StorageReference mStorage;
    FirebaseAuth.AuthStateListener mAuthListener;
    AdapterAvatar adapterAvatar;
    String referralURL = "";
    public String[] imageList = {
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit1.jpg?alt=media&token=c420dce9-7ace-42f1-9fa2-9b9450230959",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit2.jpg?alt=media&token=4a1187ae-a0f0-4aa0-8c3e-edce32310f8e",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit3.jpg?alt=media&token=41366058-a93f-4090-ac95-b030a004b5cc",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit4.jpg?alt=media&token=16232610-aa99-47bb-becc-ca7bcc0d9556",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit5.jpg?alt=media&token=4096819b-491e-4652-af3a-856e08c1522b",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit6.jpg?alt=media&token=fbf7832e-357d-45a0-b90b-7d383e1af84c",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit7.jpg?alt=media&token=b018a286-d008-42f1-af0a-cb84126a306f",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit8.jpg?alt=media&token=05280825-3eed-4405-9fc2-1c9d8a916206",
            "https://firebasestorage.googleapis.com/v0/b/livestreaming-4f7f3.appspot.com/o/avatars%2Ffruit9.jpg?alt=media&token=befccdcf-ef37-4d72-b433-45ab57d20708"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.sign_up_get_data);
        initialViews();

        mStorage = FirebaseStorage.getInstance().getReference().child("avatars");
        filePath = Uri.parse(imageList[(int)(Math.random()*5)]);
        Glide.with(SignUpFormActivity.this).load(filePath).into(profileimageView);
        intent = getIntent();
        viewDialog = new ViewDialog(this);
        // get ids from layout

        if(mAuth.getCurrentUser().getEmail()!= null){
            if(mAuth.getCurrentUser().getEmail().length()>0){
                email.setText(mAuth.getCurrentUser().getEmail().toString());
                email.setInputType(InputType.TYPE_NULL);
                email.setCompoundDrawables(null, null, null, null);
            }
        }

        if (intent.hasExtra("gName")) {
            gNaame = getIntent().getStringExtra("gName");
            gEmaail = getIntent().getStringExtra("gEmail");
            String gid = getIntent().getStringExtra("gImg");
            username.setText(gNaame);
            email.setText(gEmaail);
            email.setEnabled(false);
            email.setTextColor(Color.GRAY);
            email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        RecyclerView recyclerView = findViewById(R.id.avatarImages);
        int numberOfColumns = 5;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapterAvatar = new AdapterAvatar(this, imageList);
        adapterAvatar.setClickListener(this);
        recyclerView.setAdapter(adapterAvatar);

        singupactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _username = username.getText().toString().trim();
                _email = email.getText().toString().trim();
                if (!validateUserName() | !validateimage() | !validateEmail()) {
                    return;
                } else {
                    viewDialog.showDialog();

                    if(ImageUploaded) uplnamephoto();
                    else AddData(String.valueOf(filePath));
                }
            }
        });
    }

    private void AddData(final String urlimg) {
        Toast.makeText(this, "url", Toast.LENGTH_SHORT).show();
        String Userid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        referralURL = sb.toString();

        User obj = new User(_username, _email, urlimg, "100", "0", referralURL);
        StaticConfig.user = obj;
        FirebaseDatabase.getInstance().getReference("Users").child(Userid).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(SignUpFormActivity.this, MainActivity.class);
                if (getIntent().hasExtra("gName")) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("GSign", "yes");
                    editor.commit();
                }
                viewDialog.hideDialog();
                startActivity(intent);
                finish();

            }
        });
    }

    private void initialViews() {
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        profileimageView = findViewById(R.id.imageViewprofile);
        singupactive = findViewById(R.id.singupactive);
    }

    // validate functions
    private boolean validateEmail() {

        if (_email.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else {
            Remove_Error(email);
            return true;
        }
    }

    private boolean validateUserName() {
        if (_username.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (_username.length() > 30) {
            username.setError("user name is too long");
            return false;
        } else {
            Remove_Error(username);
            return true;
        }
    }

    private boolean validateimage() {
        if (filePath != null) {
            return true;
        } else {
            Toast.makeText(this, "Please Choose image", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private void Remove_Error(EditText e) {
        e.setError(null);
    }

    public void ChangeImage(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(1000, 1000, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                .start(SignUpFormActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                filePath = resultUri;
                // Toast.makeText(this, "\n"+filePath.toString(), Toast.LENGTH_SHORT).show();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    profileimageView.setImageBitmap(bitmap);
                    imgpath = encodeTobase64(bitmap);
                    ImageUploaded = true;


                } catch (IOException e) {
                    e.printStackTrace();
                }// getImageUri( resultUri,1);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uplnamephoto() {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(SignUpFormActivity.this);
        String url = "https://auxiliumlivestreaming.000webhostapp.com/addphoto.php";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("error");
                    if (!status) {
                        finalImage = jsonObject.getString("thumb");

                        AddData(finalImage);


                    } else {

                        Toast.makeText(SignUpFormActivity.this, "Error While Uploading Server Issue", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Toast.makeText(SignUpFormActivity.this, "Json", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                if (error != null)
                    Log.e("error", error.getLocalizedMessage() + "");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("photo", imgpath); //Add the data you'd like to send to the server.
                MyData.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()); //Add the data you'd like to send to the server.

                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }


    public String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    public void onItemClick(View view, int position) {
        filePath= Uri.parse(imageList[position]);
        Glide.with(SignUpFormActivity.this).load(imageList[position]).into(profileimageView);
    }
}