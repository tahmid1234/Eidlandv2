package com.eidland.auxilium.voice.only.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.agora.rtc.Constants;

public class EnterRoomActivity extends AppCompatActivity {
    ImageView userimg;
    EditText txttitle;
    Button btncreate;
    String imgpath;
    Uri filePath;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_room);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please Wait...!");
        progressDialog.setCancelable(false);

        btncreate = findViewById(R.id.btncreate);
        txttitle = findViewById(R.id.txttitle);
        userimg = findViewById(R.id.userimage);
        if (StaticConfig.user != null) {
            Glide.with(this).load(StaticConfig.user.imageurl).error(R.drawable.appicon).into(userimg);
        }
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txttitle.getText().toString().trim().length() <= 0) {
                    Toast.makeText(EnterRoomActivity.this, "Enter Title Please", Toast.LENGTH_SHORT).show();
                } else {
                    if (imgpath != null) {
                        uplnamephoto();
                    } else {
                        Intent intent = new Intent(EnterRoomActivity.this, LiveRoomActivity.class);
                        intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_BROADCASTER);
                        intent.putExtra("User", "Host");
                        intent.putExtra("UserName", txttitle.getText().toString());
                        intent.putExtra("profile", StaticConfig.user.getImageurl());
                        intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, new Random().nextInt() + FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void uplnamephoto() {
        progressDialog.show();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(EnterRoomActivity.this);
        String url = "https://auxiliumlivestreaming.000webhostapp.com/addphoto.php";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("error");
                    if (!status) {
                        String url = jsonObject.getString("thumb");
                        Intent intent = new Intent(EnterRoomActivity.this, LiveRoomActivity.class);
                        intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_BROADCASTER);
                        intent.putExtra("User", "Host");
                        intent.putExtra("UserName", txttitle.getText().toString());
                        intent.putExtra("profile", url);
                        intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, new Random().nextInt() + FirebaseAuth.getInstance().getCurrentUser().getUid());
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();

                    } else {

                        Toast.makeText(EnterRoomActivity.this, "Error While Uploading Server Issue", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Toast.makeText(EnterRoomActivity.this, "Json", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                if (error != null)
                    Log.e("error", error.getLocalizedMessage() + "");

                progressDialog.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("photo", imgpath); //Add the data you'd like to send to the server.
                MyData.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()); //Add the data you'd like to send to the server.

                return MyData;
            }
        };

        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }

    public void ChangeImage(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(1000, 1000, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                .start(EnterRoomActivity.this);
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
                    userimg.setImageBitmap(bitmap);
                    imgpath = encodeTobase64(bitmap);
                    Log.e("path", imgpath);

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

    public String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
}