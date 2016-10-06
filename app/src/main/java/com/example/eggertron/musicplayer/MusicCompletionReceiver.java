package com.example.eggertron.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by eggertron on 10/6/16.
 */
public class MusicCompletionReceiver extends BroadcastReceiver {

    MainActivity mainActivity;

    /*
        Constructor: needs to reference the mainActivity to update it.
     */
    public MusicCompletionReceiver (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /*
        we retrieve the music name, and update the TextView.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String musicName = intent.getStringExtra(MusicService.MUSICNAME);
        mainActivity.updateName(musicName);
    }
}
