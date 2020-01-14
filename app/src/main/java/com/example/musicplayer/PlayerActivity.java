package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next;
    Button btn_previous;
    Button btn_pause;
    TextView songTextLevel;
    SeekBar songSeekBar;


    static MediaPlayer myMediaplayer;
    int position;
    String sname;

    ArrayList<File> mySongs;
    Thread updateSeekBar;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next = (Button) findViewById(R.id.next);
        btn_previous = (Button) findViewById(R.id.previous);
        btn_pause = (Button) findViewById(R.id.pause);

        songTextLevel = (TextView) findViewById(R.id.songLevel);
        songSeekBar = (SeekBar) findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = myMediaplayer.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = myMediaplayer.getCurrentPosition();
                        songSeekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }

        };


        if (myMediaplayer != null) {
            myMediaplayer.stop();
            myMediaplayer.release();

        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        sname = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");

        songTextLevel.setText(songName);
        songTextLevel.setSelected(true);

        position = bundle.getInt("pos", 0);


        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaplayer = MediaPlayer.create(getApplicationContext(), u);
        myMediaplayer.start();
        songSeekBar.setMax(myMediaplayer.getDuration());

        updateSeekBar.start();
        songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaplayer.seekTo(seekBar.getProgress());


            }
        });


        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekBar.setMax(myMediaplayer.getDuration());

                if (myMediaplayer.isPlaying()) {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaplayer.pause();
                } else {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaplayer.start();

                }


            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaplayer.stop();
                myMediaplayer.release();
                position = ((position + 1) % mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaplayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mySongs.get(position).getName().toString();
                songTextLevel.setText(sname);
                myMediaplayer.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaplayer.stop();
                myMediaplayer.release();
                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaplayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mySongs.get(position).getName().toString();
                songTextLevel.setText(sname);
                myMediaplayer.start();
            }
        });

    }

}

