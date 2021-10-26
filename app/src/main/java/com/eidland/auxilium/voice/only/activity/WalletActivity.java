package com.eidland.auxilium.voice.only.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.helper.Helper;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler {
    TextView txtcurrent;
    LinearLayout buy50, buy1000, buy350;
    BillingProcessor billingProcessor;
    String coincomma, Userid;
    ViewDialog viewDialog;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        buy50 = findViewById(R.id.buy50);
        buy1000 = findViewById(R.id.buy1000n);
        buy350 = findViewById(R.id.buy350);

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

    public void initpur() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if (isAvailable) {
            billingProcessor = new BillingProcessor(this, getResources().getString(R.string.Playid), this);
            billingProcessor.initialize();
        }
    }


    @Override
    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentCoins = StaticConfig.user.getCoins();
        long coininnumber = Long.parseLong(currentCoins);
        long finalcoin = coininnumber + Long.parseLong(productId);
        StaticConfig.user.setCoins(finalcoin + "");

        verifypurchase();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    ProgressDialog progressDialog;

    public void verifypurchase() {
        viewDialog.showDialog("Please wait for a while");
        FirebaseDatabase.getInstance().getReference("Users").child(Userid).setValue(StaticConfig.user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(WalletActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                coincomma = Helper.getFormattedText(StaticConfig.user.getCoins());
                txtcurrent.setText(coincomma);
                Toast.makeText(WalletActivity.this, "Purchased Successfully", Toast.LENGTH_SHORT).show();
                // lrnmethod.setVisibility(View.GONE);
                viewDialog.hideDialog();


            }
        });
    }


    String selectedkey = "";

    @Override
    public void onClick(View v) {
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
    }

}