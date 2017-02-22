package com.szumusic.szumusicapp.ui.music.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kobe_xuan on 2017/2/21.
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       Intent intent1=new Intent("UPDATE_PLAYER");
        intent1.putExtra("type",2);
        context.sendBroadcast(intent1);

    }
}
