package com.eidland.auxilium.voice.only.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.adapter.CommentAdapter;
import com.eidland.auxilium.voice.only.adapter.ViewerListAdapter;
import com.eidland.auxilium.voice.only.model.Viewer;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewDialogUser {

    Activity activity;
    Dialog dialog;
    RecyclerView recyclerView;
    ArrayList<Viewer> viewerArrayList;
    ViewerListAdapter viewerListAdapter;

    public ViewDialogUser(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(ArrayList<Viewer> instruction) {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.online_users);
        dialog.getWindow().setLayout(600, 700);
        recyclerView = dialog.findViewById(R.id.userrecycler);
        ImageView cross = dialog.findViewById(R.id.close);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        this.viewerArrayList = instruction;
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        viewerListAdapter = new ViewerListAdapter(activity, viewerArrayList);
        recyclerView.setAdapter(viewerListAdapter);
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
}
