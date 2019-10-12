package com.example.ladiesfirst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class MyBroadCastReceiver extends BroadcastReceiver {

    MediaPlayer mediaPlayer = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        mediaPlayer.stop();
        Log.d("Vibrator", "Hoping to stop!");
    }
}