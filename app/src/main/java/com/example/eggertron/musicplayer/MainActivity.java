package com.example.eggertron.musicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String INITIALIZE_STATUS = "initialization status";
    public static final String MUSIC_PLAYING = "music playing";

    private Button playBtn;
    private TextView playTxt;

    MusicService musicService;
    MusicCompletionReceiver musicBroadcastReceiver;
    Intent startMusicServiceIntent;
    boolean isInitialized = false;
    boolean isBound = false;

    //This class is used to bind the activity to the service. Once bound, we retrieve the service.
    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder binder = (MusicService.MyBinder) service;
            musicService = binder.getService();
            isBound = true;
            switch (musicService.getPlayingStatus()) {
                case 0:
                    playBtn.setText("Start");
                    break;
                case 1:
                    playBtn.setText("Pause");
                    break;
                case 2:
                    playBtn.setText("Resume");
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button)findViewById(R.id.play);
        playTxt = (TextView)findViewById(R.id.music);

        playBtn.setOnClickListener(this);
        //Create the MusicService Class.
        //Create the MusicCompletionReceiver Class.
        //Add Permissions, Services and BroadcastReceivers to the manifest.
        //Create MusicPlayer Class to control music.

        startMusicServiceIntent = new Intent(this, MusicService.class);

        if (!isInitialized) {
            startService(startMusicServiceIntent);
            isInitialized = true;
        }

        musicBroadcastReceiver = new MusicCompletionReceiver(this);

        //try to retrieve savedInstanceState data if it is resuming.
        if (savedInstanceState != null) {
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
            playTxt.setText(savedInstanceState.getString(MUSIC_PLAYING));
        }

    } // onCreate

    /*
        In the onResume() method, we bind the service, and register the BroadcastReceiver.
     */
    protected void onResume() {
        super.onResume();
        if (isInitialized && !isBound) {
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
        }
        registerReceiver(musicBroadcastReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
    }

    /*
        In the onPause() method, we unbind the service, and unregister the BroadcastReceiver.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (isBound) {
            unbindService(musicServiceConnection);
            isBound = false;
        }
        unregisterReceiver(musicBroadcastReceiver);
    }

    public void updateName(String name) {
        playTxt.setText(name);
    }

    /*
        The onClick method, based on which state the music is at,
        We call different music controlling method in the music service.
     */
    @Override
    public void onClick(View v) {
        if (isBound) {
            switch (musicService.getPlayingStatus()) {
                case 0:
                    musicService.startMusic();
                    playBtn.setText("Pause");
                    break;
                case 1:
                    musicService.pauseMusic();
                    playBtn.setText("Resume");
                    break;
                case 2:
                    musicService.resumeMusic();
                    playBtn.setText("Pause");
                    break;
            }
        }
    } // onClick()

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, playTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
