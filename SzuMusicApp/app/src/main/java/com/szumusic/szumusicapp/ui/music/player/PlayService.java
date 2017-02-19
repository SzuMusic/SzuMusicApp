package com.szumusic.szumusicapp.ui.music.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.IOException;

public class PlayService extends Service {

    private MediaPlayer mediaPlayer=new MediaPlayer();
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocation();
        mLocationClient.start();
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
    //返回歌曲当前duration
    public int getCurrentDuration(){
        return mediaPlayer.getCurrentPosition();
    }
    //返回歌曲的总长度
    public int getDuration(){
        return mediaPlayer.getDuration();
    }
    //返回歌曲是否处于播放状态
    public boolean getState(){
        return mediaPlayer.isPlaying();
    }
    //设置播放进度
    public void setProgress(int progress){
        mediaPlayer.seekTo(progress);

    }
    //下面是百度定位的初始化
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=10000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("执行了PlayService的onDestroy方法");
    }
}
