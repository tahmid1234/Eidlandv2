package com.eidland.auxilium.voice.only;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.agora.rtc.Constants;

import com.chaos.view.PinView;
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
import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Staticconfig;
import com.eidland.auxilium.voice.only.ui.LiveRoomActivity;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class VerifyotpActivity extends AppCompatActivity {
    TextView setphonenumber;
    PinView pin;
    RelativeLayout verifyotp;
    String otpid, phoneNumber, error, pincode;
    ProgressDialog dialoge;
    ImageView backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

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
        AlertDialog.Builder dialoge = new AlertDialog.Builder(VerifyotpActivity.this);
        dialoge.setTitle("Welcome to Eidland")
                .setMessage("Please ensure your microphone and storage permission is given in order to get most out of Eidland")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean checkPermissionResult = checkSelfPermissions();
                        if (checkPermissionResult) {
                            try{
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, pincode);
                                signInWithPhoneAuthCredential(credential);
                            }catch (Exception e){
                                System.out.println(e);
                            }
                            dialog.cancel();
                        } else Log.e("no permission", "Not Found");
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void progressdialogeshow() {
        dialoge = new ProgressDialog(VerifyotpActivity.this);
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
                        ShowDialogue(VerifyotpActivity.this);
                    }
                });
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    ((AGApplication) getApplication()).initWorkerThread();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, pincode);
                    signInWithPhoneAuthCredential(credential);

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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        dialoge.show();
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        accessGranted(user);
                    }else{
                        Toast.makeText(VerifyotpActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(VerifyotpActivity.this, "Signin Code Error", Toast.LENGTH_LONG).show();
                }
                dialoge.dismiss();
            }
        });
    }


    public void ShowDialogue(VerifyotpActivity view) {
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

    public void accessGranted(FirebaseUser user){
        String userid = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Intent intent = new Intent(VerifyotpActivity.this, SignUpData.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Staticconfig.user = snapshot.getValue(User.class);
                    Intent intent = new Intent(VerifyotpActivity.this, LiveRoomActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("User", "Participent");
                    intent.putExtra("userid", "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
                    intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, "760232943A3qP5qyS34aGkFxQa3caaXxmHGl2");
                    intent.putExtra("UserName", "Eidland Battle Royale");
                    intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");
                    intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(VerifyotpActivity.this, "Success", Toast.LENGTH_SHORT).show();
    }
}