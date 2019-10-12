package com.example.ladiesfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;

public class Sendalarms extends Activity {

    private static final int SLEEP_TIME = 10000;
    private boolean IS_VIBRATE = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myBroadCastReceiver, screenStateFilter);

        if (IS_VIBRATE) {
            IS_VIBRATE = true;
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (v.hasVibrator()) {
                final long[] pattern = {0, 1000, 500, 1000, 500, 1000};
                new Thread() {
                    @Override
                    public void run() {
                        while(true) {
                                v.vibrate(pattern, -1);
                                try {
                                    Thread.sleep(SLEEP_TIME);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                }.start();
            }
        }
    }
}
