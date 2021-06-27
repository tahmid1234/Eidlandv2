package com.eidland.auxilium.voice.only;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import io.agora.rtc.Constants;

import com.eidland.auxilium.voice.only.model.ConstantApp;
import com.eidland.auxilium.voice.only.model.Staticconfig;
import com.eidland.auxilium.voice.only.ui.LiveRoomActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.ui.ViewDialog;

public class Sign_Up_Activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String status;
    CallbackManager callbackManager;
    EditText phonenumber1;
    CountryCodePicker ccp;
    ImageView iv;
    ViewDialog viewDialog;
    SharedPreferences sharedpreferences;
    RelativeLayout continueBTN;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up_);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        viewDialog = new ViewDialog(this);
        status = "vallagena";
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        phonenumber1 = findViewById(R.id.txtnumber);
        continueBTN = findViewById(R.id.phn_next);
        iv = new ImageView(getApplicationContext());
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phonenumber1);
        phonenumber1.setImeOptions(EditorInfo.IME_ACTION_GO);
        phonenumber1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    Intent intent = new Intent(Sign_Up_Activity.this, VerifyotpActivity.class);
                    intent.putExtra("mobileNumber", ccp.getFullNumberWithPlus().trim());
                    startActivity(intent);
                    return true;
                }
                return true;
            }
        });
        continueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_Up_Activity.this, VerifyotpActivity.class);
                intent.putExtra("mobileNumber", ccp.getFullNumberWithPlus().trim());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((Activity) getApplicationContext()).finish();
    }
}