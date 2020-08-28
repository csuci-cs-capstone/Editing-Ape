package com.example.capstoneapplication;


import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.SeekBar;

import android.widget.VideoView;



import java.io.File;

public class VideoEditor extends AppCompatActivity {

    Uri uri;
    ImageView imgView;
    VideoView vidView;
    SeekBar videoDurBar;

    boolean vidPlaying = false;
    int vidDuration;
    String fileName, inputVideoPath;
    String[] command;
    File destination;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_editor);


        videoDurBar= (SeekBar) findViewById(R.id.seekBarVid);
        imgView = (ImageView) findViewById(R.id.pause_icon);
        vidView = (VideoView) findViewById(R.id.videoView);

        Intent passUri = getIntent();
        if(passUri != null){

            String videoPath = passUri.getStringExtra("uri");
            uri = Uri.parse(videoPath);
            vidPlaying= true;
            vidView.setVideoURI(uri);
            vidView.start();

        }
        configureVideoTrimmerButton();
        clickListeners();
    }

    private void clickListeners(){
        //click listener for the pause button
        imgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(vidPlaying) {
                    imgView.setImageResource(R.drawable.icon_play);//changes icon to play button when paused
                    vidView.pause();
                    vidPlaying = false;
                }
                else{//if was paused, play on user click
                    vidView.start();
                    imgView.setImageResource(R.drawable.icon_pause);
                    vidPlaying= true;
                }
            }
        });
        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vidView.start();
                vidDuration =mp.getDuration()/1000; //get vid time in seconds since getDuration returns ms
                //clockLeft.setText("00:00:00");
                //clockRight.setText(getClockValue(vidDuration));
                mp.setLooping(true);

                //setting bounds for the video duration snipping bar

                videoDurBar.setMax(vidDuration);
                videoDurBar.setEnabled(true);
                videoDurBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(vidView != null && fromUser){
                            vidView.seekTo(progress * 1000);
                        }
                    }
                });
                Handler vidHandler = new Handler();
                vidHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(vidView != null){
                            int currentPos = vidView.getCurrentPosition()/1000;
                            videoDurBar.setProgress(currentPos);
                        }
                    }
                },1000);
            }
        });
    }

    public void configureVideoTrimmerButton(){
        ImageButton IEButton =  findViewById(R.id.trim_button);
        IEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTrimmer =new Intent(VideoEditor.this, VideoTrimmer.class);
                toTrimmer.putExtra("uri", uri.toString());
                startActivity(toTrimmer);

            }
        });
    }





}