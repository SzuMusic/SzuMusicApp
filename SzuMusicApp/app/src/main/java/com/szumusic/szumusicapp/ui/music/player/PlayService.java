package com.szumusic.szumusicapp.ui.music.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class PlayService extends Service {

    private MediaPlayer mediaPlayer=new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    //播放新的音乐带url
    public void playMusic(String url){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //暂停音乐
    public void pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            int duration=mediaPlayer.getDuration();
            System.out.println("duration的值为"+duration);
            System.out.println("执行了暂停函数");
        }
    }
    //暂停后重启播放音乐
    public void play(){
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
}
