package com.example.eggertron.musicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.IOException;

/**
 * Created by Edgar Han on 10/6/16.
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener { //Implement this to override onCompletion.
    static final String[] MUSICPATH = new String[] {
            "/Music/mario Here we go.mp3",
            "/Music/tetris.mp3",
    };
    static final String[] MUSICNAME = new String[] {
            "Super Mario",
            "Tetris",
    };
    MediaPlayer player; // to load, initialize and play the music.
    int currentPosition = 0;
    int musicIndex = 0;
    private int musicStatus = 0;
    private MusicService musicService;

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public int getPlayingStatus() {
        return musicStatus;
    }

    public String getMusicName() {
        return MUSICNAME[musicIndex];
    }

    /*
        initialize and play the music
     */
    public void playMusic() {
        try {
            musicService.onUpdateMusicName(getMusicName()); //Broadcast the completion event.
            player = new MediaPlayer();
            player.setDataSource(Environment.getExternalStorageDirectory() + MUSICPATH[musicIndex]);
            player.setAudioStreamType((AudioManager.STREAM_MUSIC));
            player.prepare();
            player.start();
            musicStatus = 1;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // register the listener.
        player.setOnCompletionListener(this);
    }

    /*
        just pause the music
     */
    public void pauseMusic() {
        if (player != null && player.isPlaying()) {
            player.pause();
            currentPosition = player.getCurrentPosition(); //Save position for resume method.
            musicStatus = 2;
        }
    }

    /*
        just resume the music
     */
    public void resumeMusic() {
        if (player != null) {
            player.seekTo(currentPosition);
            player.start();
            musicStatus = 1;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        musicIndex = (musicIndex + 1) % MUSICNAME.length;
        player.release();
        player = null;
        playMusic();
    } //onCompletion. from implements MediaPlayer.OnCompletionListener
}
