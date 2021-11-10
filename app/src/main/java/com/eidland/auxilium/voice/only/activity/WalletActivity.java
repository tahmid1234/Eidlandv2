package com.eidland.auxilium.voice.only.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.Helper;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtcurrent;
    LinearLayout buy50, buy1000, buy350;
    BillingProcessor billingProcessor;
    String coincomma, Userid;
    ViewDialog viewDialog;
    ImageView back;
    int width=0;
    boolean productpurchasedcalled=false;
    boolean verifycalled=false;
    String coinvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        buy50 = findViewById(R.id.buy50);
        buy1000 = findViewById(R.id.buy1000n);
        buy350 = findViewById(R.id.buy350);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        viewDialog = new ViewDialog(this);
        back = findViewById(R.id.wallet_back);

        txtcurrent = findViewById(R.id.txtname);
        coincomma = Helper.getFormattedText(StaticConfig.user.getCoins());
        txtcurrent.setText(coincomma);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buy50.setOnClickListener(this);
        buy1000.setOnClickListener(this);
        buy350.setOnClickListener(this);

        initpur();
    }

    void purchasecoins() {
billingProcessor.purchase(this, selectedkey);



    }
    @Override
    protected void onResume() {
        super.onResume();
        if (selectedkey==null)
        {

        }
        else {
            if(productpurchasedcalled)
            {

            }
            else {

                manualonProductPurchased(selectedkey,billingProcessor.getPurchaseInfo(selectedkey));
                selectedkey=null;
            }
        }


    }
    public void initpur() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if (isAvailable) {


            billingProcessor = new BillingProcessor(this, getResources().getString(R.string.Playid),  new BillingProcessor.IBillingHandler() {
                @Override
                public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo purchaseInfo) {
                    productpurchasedcalled=true;
                    if (!verifycalled)
                    {
                        Log.d("enteredwallaet", "onProductPurchased: Hello "+productId);
                        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String currentCoins = StaticConfig.user.getCoins();
                        long coininnumber = Long.parseLong(currentCoins);
                        long finalcoin = coininnumber + Long.parseLong(productId);
                        StaticConfig.user.setCoins(finalcoin + "");
                        billingProcessor.consumePurchaseAsync(selectedkey, new BillingProcessor.IPurchasesResponseListener()
                        {
                            @Override
                            public void onPurchasesSuccess()
                            {
                                //  Toast.makeText(getApplicationContext(),"Successfully consumed",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPurchasesError()
                            {
                                // Toast.makeText(getApplicationContext(),"Not consumed",Toast.LENGTH_SHORT).show();

                            }
                        });
                        selectedkey=null;
                        verifypurchase();
                    }

                }
                @Override
                public void onBillingError(int errorCode, @Nullable Throwable error) {
                    //Toast.makeText(getApplicationContext(),"onBillingError: " + Integer.toString(errorCode),Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onBillingInitialized() {
                   // Toast.makeText(getApplicationContext(),"onBillingInitiated: " ,Toast.LENGTH_SHORT).show();


                }
                @Override
                public void onPurchaseHistoryRestored() {

                }

            });
        }
    }
    public void manualonProductPurchased(@NonNull String productId, @Nullable PurchaseInfo purchaseInfo) {
        productpurchasedcalled=true;
        Log.d("enteredwallaet", "onProductPurchased: Hello "+productId);
        coinvalue=productId;
        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentCoins = StaticConfig.user.getCoins();
        long coininnumber = Long.parseLong(currentCoins);
        long finalcoin = coininnumber + Long.parseLong(productId);
        StaticConfig.user.setCoins(finalcoin + "");
        billingProcessor.consumePurchaseAsync(selectedkey, new BillingProcessor.IPurchasesResponseListener()
        {
            @Override
            public void onPurchasesSuccess()
            {
                //  Toast.makeText(getApplicationContext(),"Successfully consumed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPurchasesError()
            {
                // Toast.makeText(getApplicationContext(),"Not consumed",Toast.LENGTH_SHORT).show();

            }
        });
        selectedkey=null;
        verifypurchase();
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }



  /*  @Override
    public void onProductPurchased(String productId, PurchaseInfo purchaseInfo) {
        Log.d("enteredwallaet", "onProductPurchased: Hello "+productId);
        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentCoins = StaticConfig.user.getCoins();
        long coininnumber = Long.parseLong(currentCoins);
        long finalcoin = coininnumber + Long.parseLong(productId);
        StaticConfig.user.setCoins(finalcoin + "");

        verifypurchase();
    }*/


    ProgressDialog progressDialog;

    public void verifypurchase() {
        viewDialog.showDialog("Please wait for a while");
        FirebaseDatabase.getInstance().getReference("Users").child(Userid).setValue(StaticConfig.user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              //  Toast.makeText(WalletActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                coincomma = Helper.getFormattedText(StaticConfig.user.getCoins());
                txtcurrent.setText(coincomma);
                Toast.makeText(WalletActivity.this, "Purchase of " +coinvalue + " coins confirmed. Thank you!", Toast.LENGTH_SHORT).show();
                // lrnmethod.setVisibility(View.GONE);
                viewDialog.hideDialog();


            }
        });
        verifycalled=true;
    }


    String selectedkey = null;

    @Override
    public void onClick(View v) {
        productpurchasedcalled=false;
        verifycalled=false;
        switch (v.getId()) {

            case R.id.buy50:
                selectedkey = "50";
                purchasecoins();
                break;
            case R.id.buy350:
                selectedkey = "350";
               purchasecoins();
                break;
            case R.id.buy1000n:
                selectedkey = "1000";
               purchasecoins();
                break;


        }
       //calldialog();

    }


    void calldialog ()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_custom_dialog);
        LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
        linearLayout.setMinimumWidth((int) (width * 0.8));
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.dialog_icon);
        imageView.setImageResource(R.drawable.ic_sleep);
        imageView.setVisibility(View.GONE);

        TextView msg = dialog.findViewById(R.id.msg);
        msg.setText("Coin purchases are coming very soon");

        TextView negative = dialog.findViewById(R.id.positive_btn);
        RelativeLayout areatop = dialog.findViewById(R.id.topdialogbutton);
        negative.setVisibility(View.VISIBLE);
        negative.setText("OKAY");
        areatop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}