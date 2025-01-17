package com.eidland.auxilium.voice.only.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.eidland.auxilium.voice.only.AGApplication;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VerifyOTPActivity extends AppCompatActivity {
    TextView setphonenumber;
    PinView pin;
    RelativeLayout verifyotp;
    String otpid, phoneNumber, error, pincode;
    ProgressDialog dialoge;
    ImageView backBTN;
    int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        backBTN = findViewById(R.id.back_to_phn);
        pin = findViewById(R.id.pin);
        verifyotp = findViewById(R.id.verifyotp);
        setphonenumber = findViewById(R.id.setphonenumber);

        phoneNumber = getIntent().getStringExtra("mobileNumber");
        setphonenumber.setText(phoneNumber);

        progressdialogeshow();

        sendotp(phoneNumber);

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pincode = pin.getText().toString();
                if (pincode.isEmpty()) {
                    pin.setError("Blank Field can not be processed");
                } else if (pincode.length() != 6) {
                    pin.setError("Length of OTP code must be 6");
                } else {
                    pin.setError(null);
                    Callalert();
                }
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void Callalert() {

        Dialog dialog = new Dialog(VerifyOTPActivity.this);
        dialog.setContentView(R.layout.layout_custom_dialog);
        LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
        linearLayout.setMinimumWidth((int) (width * 0.8));
      //  dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
        dialog.setCancelable(false);


        TextView title = dialog.findViewById(R.id.dialog_title);
        title.setVisibility(View.VISIBLE);
        title.setText("Welcome to Eidland");

        TextView msg = dialog.findViewById(R.id.msg);
        msg.setVisibility(View.VISIBLE);
        msg.setText("Please ensure your microphone and storage permissions are given in order to get the most out of Eidland");

        TextView positive = dialog.findViewById(R.id.positive_btn);
        positive.setVisibility(View.VISIBLE);
        positive.setText("Okay");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, pincode);
                signInWithPhoneAuthCredential(credential);
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void progressdialogeshow() {
        dialoge = new ProgressDialog(VerifyOTPActivity.this);
        dialoge.setMessage("Loading, Please Wait!");
        dialoge.setCancelable(false);
    }

    private void sendotp(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        dialoge.cancel();
                        if (code != null) {
                            pin.setText(code);
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpid = s;
                        dialoge.cancel();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        dialoge.cancel();
                        error = e.getMessage();
                        ShowDialogue(VerifyOTPActivity.this);
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        dialoge.show();
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        accessGranted(user);
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "Signin Code Error", Toast.LENGTH_LONG).show();
                }
                dialoge.dismiss();
            }
        });
    }


    public void ShowDialogue(VerifyOTPActivity view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(view);
        dialog.setTitle("Error");
        dialog.setCancelable(false);
        dialog.setMessage(error);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        dialog.show();
    }

    public void accessGranted(FirebaseUser user) {
        String userid = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Intent intent = new Intent(VerifyOTPActivity.this, SignUpFormActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    StaticConfig.user = snapshot.getValue(User.class);
                    Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Intent intent = new Intent(VerifyOTPActivity.this, LiveRoomActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("User", "Participent");
//                    intent.putExtra("userid", "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
//                    intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, "760232943A3qP5qyS34aGkFxQa3caaXxmHGl2");
//                    intent.putExtra("UserName", "Eidland Battle Royale");
//                    intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");
//                    intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);

                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(VerifyOTPActivity.this, "Success", Toast.LENGTH_SHORT).show();
    }
}