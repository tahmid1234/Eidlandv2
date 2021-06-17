package com.eidland.auxilium.voice.only;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.eidland.auxilium.voice.only.adapter.ImageAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageSelectActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
    }

    public void ChangeImage(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setRequestedSize(1000, 1000, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                .start(ImageSelectActivity.this);

    }
}
