package com.eidland.auxilium.voice.only.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {
    TextView tvname, tvmail;
    ImageView imageViewuphoto;
    String userid, ImageUrl, imgpath;
    Uri filePath;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        viewDialog = new ViewDialog(this);

        tvname = findViewById(R.id.username);
        tvmail = findViewById(R.id.userEmail);
        imageViewuphoto = findViewById(R.id.userimage);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tvname.setText(StaticConfig.user.getName());
        tvmail.setText(StaticConfig.user.getEmail());
        ImageUrl = StaticConfig.user.getImageurl();
        Glide.with(ProfileEditActivity.this).load(StaticConfig.user.getImageurl()).into(imageViewuphoto);
    }

    public void SignOut(View view) {

    }

    public void update(View view) {

        viewDialog.showDialog("We are updating your profile information. Please wait..");
        if (filePath == null) {
            AddData(ImageUrl);
        } else {
            uplnamephoto();
        }

    }

    private void AddData(String url) {
        String Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        User obj = new User(tvname.getText().toString(), tvmail.getText().toString(), url, StaticConfig.user.getCoins(), StaticConfig.user.getReceivedCoins(), StaticConfig.user.getReferralURL(), StaticConfig.user.getReferrer());
        StaticConfig.user = obj;
        FirebaseDatabase.getInstance().getReference("Users").child(Userid).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProfileEditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                viewDialog.hideDialog();
                onBackPressed();

            }
        });
    }

    public void uplnamephoto() {
        viewDialog.showDialog("Please wait for a while");
        RequestQueue MyRequestQueue = Volley.newRequestQueue(ProfileEditActivity.this);
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
                        AddData(url);
                    } else {

                        Toast.makeText(ProfileEditActivity.this, "Error While Uploading Server Issue", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Toast.makeText(ProfileEditActivity.this, "Json", Toast.LENGTH_SHORT).show();
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

    public void ChangeImage(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(1000, 1000, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                .start(ProfileEditActivity.this);
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
                    imageViewuphoto.setImageBitmap(bitmap);
                    imgpath = encodeTobase64(bitmap);


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