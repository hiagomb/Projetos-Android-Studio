package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPlay, btnPause, btnStop;
    private SeekBar seek_vol;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay= findViewById(R.id.btn_play);
        btnPause= findViewById(R.id.btn_pause);
        btnStop= findViewById(R.id.btn_stop);
        seek_vol= findViewById(R.id.seek_volume);
        mediaPlayer= MediaPlayer.create(getApplicationContext(), R.raw.teste);

        init_seekBar();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!= null){
                    mediaPlayer.start();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer= MediaPlayer.create(getApplicationContext(), R.raw.teste);
                }
            }
        });
    }
    
    private void init_seekBar(){
        //audio Manager
        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max_vol= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int now_vol= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seek_vol.setMax(max_vol);
        seek_vol.setProgress(now_vol);
        seek_vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}