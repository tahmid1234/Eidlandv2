package com.eidland.auxilium.voice.only.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.eidland.auxilium.voice.only.R;

public class ViewDialog {

    Activity activity;
    Dialog dialog;

    public ViewDialog(Activity activity) {
        this.activity = activity;
    }


    public void showDialog(String instruction) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.wait_pop_up);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        TextView textView = dialog.findViewById(R.id.textViewpopup);
        textView.setText(instruction);

        Glide.with(this.activity)
                .load(R.drawable.giphyloop)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(gifImageView));

        dialog.show();
    }

    public void showDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.wait_pop_up);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        Glide.with(this.activity)
                .load(R.drawable.giphyloop)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(new DrawableImageViewTarget(gifImageView));

        dialog.show();
    }

    public void hideDialog() {
        dialog.dismiss();
    }

}
