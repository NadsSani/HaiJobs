package com.starwings.app.haijobs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.starwings.app.haijobs.api.ApiLinks;

public class ProfileImageViewer extends HaiJobsActivity {
    ImageView fullImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile Image Preview");
        setContentView(R.layout.activity_image_viewer);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fullImageView=(ImageView)findViewById(R.id.fullimage);
        Glide.with(this).load(getIntent().getStringExtra("Path")).into(fullImageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
