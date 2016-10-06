package com.example.eggertron.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by eggertron on 10/6/16.
 */
public class MusicService extends Service {

    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSICNAME = "music name";

    MusicPlayer musicPlayer;

    //We need a Binder to let the activity attach to the MusicService and thereafter control the music.
    private final IBinder iBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MusicPlayer();
        musicPlayer.setMusicService(this);
    }

    public void startMusic() {
        musicPlayer.playMusic();
    }

    public void pauseMusic() {
        musicPlayer.pauseMusic();
    }

    public void resumeMusic() {
        musicPlayer.resumeMusic();
    }

    public int getPlayingStatus() {
        return musicPlayer.getPlayingStatus();
    }

    /*
        broadcasts the complete event. Uses Intent to send broadcast to update the UI.
     */
    public void onUpdateMusicName (String musicname) {
        Intent intent = new Intent (COMPLETE_INTENT);
        intent.putExtra(MUSICNAME, musicname);
        sendBroadcast(intent);
    }
}
