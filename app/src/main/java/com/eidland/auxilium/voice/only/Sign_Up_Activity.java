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
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    String userid, userName, userEmail, userImage, Coins = "1000";
    User userobj;
    FirebaseAuth mAuth;
    String status;
    CallbackManager callbackManager;
    AuthCredential credential;
    EditText phonenumber1;
    RelativeLayout googlesign;
    CountryCodePicker ccp;
    TextView textView;
    ImageView iv;
    ViewDialog viewDialog;
    SharedPreferences sharedpreferences;
    RelativeLayout continueBTN;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

//        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up_);

        // Firebase Authentication Initilize
        mAuth = FirebaseAuth.getInstance();
        // textView=findViewById(R.id.eidland);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //    Spannable spannable = new SpannableString("EIDLAND");
        // spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), 3, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //textView.setText(spannable);
        // ProgressDialoge Create
        viewDialog = new ViewDialog(this);
        // Request to google for Google Sign in
        Google_Signin_Request();
        status = "vallagena";
        sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);

        phonenumber1 = findViewById(R.id.txtnumber);
        continueBTN = findViewById(R.id.phn_next);
        googlesign = findViewById(R.id.googlesignup);
//        TextView textView = (TextView) googlesign.getChildAt(0);

        //  textView.setText("Sign in with Google");

        //textView.setTextSize(16);
        iv = new ImageView(getApplicationContext());
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phonenumber1);
        phonenumber1.setImeOptions(EditorInfo.IME_ACTION_GO);
        googlesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDialog.showDialog("You are now entering Eidland. Enjoy!");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
        phonenumber1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    // Your action on done

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


    // onClick Facebook Login Button
    public void facebooklogin(View view) {
        viewDialog.showDialog("You are now entering Eidland. Enjoy!");
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(Sign_Up_Activity.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                progressDialog.cancel();
            }

            @Override
            public void onError(FacebookException error) {
                viewDialog.hideDialog();
                Toast.makeText(Sign_Up_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d("tag", "handleFacebookAccessToken:" + token);
        credential = FacebookAuthProvider.getCredential(token.getToken());
        getFbInfo(token);

    }

    private void getFbInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=large";
                            String email = id;
                            if (object.has("email")) {
                                email = object.getString("email");
                            }
                            userName = first_name + " " + last_name;
                            userEmail = email;
                            userImage = image_url;
                          //  CreateUser();

                        } catch (JSONException e) {
                            viewDialog.hideDialog();
                            Toast.makeText(Sign_Up_Activity.this, "There is Problem with your facebook Please Login with Phone", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                viewDialog.hideDialog();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void CreateUser() {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                            if (sharedpreferences.contains("GSign")) {
                                status = sharedpreferences.getString("GSign", " ");
                                if (status.isEmpty()) status = "no";
                            }

                            if (isNew || status.equals("no")) {
                                Intent intent = new Intent(Sign_Up_Activity.this, SignUpData.class);
                                intent.putExtra("gName", userName);
                                intent.putExtra("gEmail", userEmail);
                                intent.putExtra("gChobi", userImage);

                                startActivity(intent);
                                finish();
                            } else {

                                userid = mAuth.getCurrentUser().getUid();

                                User obj = new User(userName, userEmail, String.valueOf(mAuth.getCurrentUser().getPhotoUrl()), "100", "0");
                                Staticconfig.user = obj;
                                Intent intent = new Intent(Sign_Up_Activity.this, LiveRoomActivity.class);
                                intent.putExtra("User", "Participent");
                                intent.putExtra("userid", "cJupIaBOKXN8QqWzAQMQYFwHzVC3");
                                intent.putExtra(ConstantApp.ACTION_KEY_ROOM_NAME, "760232943A3qP5qyS34aGkFxQa3caaXxmHGl2");

                                intent.putExtra("UserName", "Eidland Battle Royale");
                                intent.putExtra("profile", "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png");

                                intent.putExtra(ConstantApp.ACTION_KEY_CROLE, Constants.CLIENT_ROLE_AUDIENCE);
                                startActivity(intent);

                                finish();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            viewDialog.hideDialog();
                            Toast.makeText(Sign_Up_Activity.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // onClick Goole Login Button
    public void google_login(View view) {

    }

    private void firebaseAuthWithGoogle(String idToken) {
        credential = GoogleAuthProvider.getCredential(idToken, null);
        userName = account.getDisplayName();
        userEmail = account.getEmail();
        userImage = account.getPhotoUrl().toString();


        //CreateUser();

    }

    private void Google_Signin_Request() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((Activity) getApplicationContext()).finish();
    }
/*  private void Progressdialog() {
        progressDialog = new ProgressDialog(this);

        progressDialog.setContentView(R.layout.wait_pop_up);

        ...initialize the imageView form infalted layout
        ImageView gifImageView = progressDialog.findViewById(R.id.custom_loading_imageView);


        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView



        progressDialog.setTitle("Please Wait...!");
        progressDialog.setMessage("Processing Data...!");
        progressDialog.setCancelable(false);
    }
*/


}