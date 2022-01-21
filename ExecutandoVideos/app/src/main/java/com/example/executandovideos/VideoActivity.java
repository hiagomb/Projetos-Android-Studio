package com.example.executandovideos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vv= findViewById(R.id.videoView);
        vv.setMediaController(new MediaController(this));//controlador
        vv.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.video);
        vv.start();

        getSupportActionBar().hide();//hide ActionBar

        View decorView= getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}