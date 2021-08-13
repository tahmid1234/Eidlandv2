package com.eidland.auxilium.voice.only.activity;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import ch.qos.logback.core.encoder.EchoEncoder;

public class RedirectToRoomActivity extends AppCompatActivity {

    Uri mInvitationUrl;
    Context mContext;
    public RedirectToRoomActivity(Context mContext){
        this.mContext = mContext;
    }

    public void createRoomLink(String roomID){
        Toast.makeText(getApplicationContext(), "Entered activity", Toast.LENGTH_SHORT).show();
        try{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            String link = "https://room.eidland.com/?roomname=" + roomID;
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(link))
                    .setDomainUriPrefix("https://eidland.page.link")
                    .setAndroidParameters(
                            new DynamicLink.AndroidParameters.Builder("com.example.android")
                                    .setMinimumVersion(125)
                                    .build())
                    .buildShortDynamicLink()
                    .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                        @Override
                        public void onSuccess(ShortDynamicLink shortDynamicLink) {
                            try {
                                mInvitationUrl = shortDynamicLink.getShortLink();
                                assert mInvitationUrl != null;
                                Toast.makeText(RedirectToRoomActivity.this, mInvitationUrl.toString(), Toast.LENGTH_LONG).show();
                                Intent sendIntent = new Intent();
                                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, mInvitationUrl);
                                sendIntent.setType("text/plain");
                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                startActivity(shareIntent);
                                Toast.makeText(RedirectToRoomActivity.this, mInvitationUrl.toString(), Toast.LENGTH_LONG).show();
                            } catch (Exception e){
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }catch (Exception e){
            Toast.makeText(RedirectToRoomActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
