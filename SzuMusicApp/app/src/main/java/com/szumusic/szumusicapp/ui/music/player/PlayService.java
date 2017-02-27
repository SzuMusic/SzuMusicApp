package com.szumusic.szumusicapp.ui.music.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.RemoteViews;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {
    private static int NOTIFICATE_ID=2017;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private IntentFilter intentFilter=new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    //private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private List<Music> musicList=new ArrayList<Music>();//播放歌曲列表
    private int songPosition=0;
    PlayServiceReceiver playServiceReceiver =new PlayServiceReceiver();
    Notification playNotification=null;
    NotificationManager notificationManager;
    RemoteViews remoteViews;//Notification的大视图
    RemoteViews contentViews;//Notification的小视图



    @Override
    public void onCreate() {
        super.onCreate();
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocation();
        mLocationClient.start();
        //registerReceiver(mNoisyReceiver,intentFilter);
        mediaPlayer.setOnCompletionListener(this);
        IntentFilter intentFilter=new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        //IntentFilter intentFilter_outCall=new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        IntentFilter intentFilter_notification=new IntentFilter("NOTIFICATION_PLAY");
        registerReceiver(playServiceReceiver,intentFilter_notification);
        registerReceiver(playServiceReceiver,intentFilter);
        //registerReceiver(playServiceReceiver,intentFilter_outCall);
        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    //下面是百度定位的初始化
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=12000;
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

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        System.out.println("进入了播放结束的函数");
        playNext(mediaPlayer);
    }

    //播放下一首(共有方法，不带参数，只供调用)
    public void next(){
       playNext(mediaPlayer);
    }

    //播放下一首(私有方法，带参数)
    private void playNext(MediaPlayer mediaPlayer) {
        songPosition++;
        System.out.println("songPosition为"+songPosition);
        System.out.println("musicList的size为"+musicList.size());
        if(songPosition>=musicList.size())
            songPosition=0;
        Music music=musicList.get(songPosition);
        Intent intent=new Intent("UPDATE_PLAYER");
        intent.putExtra("type",5);
        intent.putExtra("name",music.getTitle());
        intent.putExtra("singer",music.getArtist() + "-" + music.getAlbum());
        getApplicationContext().sendBroadcast(intent);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(music.getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Intent playFragment=new Intent("UPDATE_FRAGMENT");
            playFragment.putExtra("type",1);
            playFragment.putExtra("name",music.getTitle());
            playFragment.putExtra("singer",music.getArtist() + "-" + music.getAlbum());
            playFragment.putExtra("currentDuration",getCurrentDuration());
            playFragment.putExtra("total",getDuration());
            getApplicationContext().sendBroadcast(playFragment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        remoteViews.setTextViewText(R.id.title,music.getTitle());
        remoteViews.setTextViewText(R.id.singer,music.getArtist()+"-"+music.getAlbum());
        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play_pause);
        notificationManager.notify(NOTIFICATE_ID,playNotification);
    }

    //播放上一首
    public void playPrev(){
        songPosition--;
        System.out.println("songPosition为"+songPosition);
        System.out.println("musicList的size为"+musicList.size());
        if(songPosition>=musicList.size()||songPosition<0)
            songPosition=0;
        Music music=musicList.get(songPosition);
        Intent intent=new Intent("UPDATE_PLAYER");
        intent.putExtra("type",5);
        intent.putExtra("name",music.getTitle());
        intent.putExtra("singer",music.getArtist() + "-" + music.getAlbum());
        getApplicationContext().sendBroadcast(intent);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(music.getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Intent playFragment=new Intent("UPDATE_FRAGMENT");
            playFragment.putExtra("type",1);
            playFragment.putExtra("name",music.getTitle());
            playFragment.putExtra("singer",music.getArtist() + "-" + music.getAlbum());
            playFragment.putExtra("currentDuration",getCurrentDuration());
            playFragment.putExtra("total",getDuration());
            getApplicationContext().sendBroadcast(playFragment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        remoteViews.setTextViewText(R.id.title,music.getTitle());
        remoteViews.setTextViewText(R.id.singer,music.getArtist()+"-"+music.getAlbum());
        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play_pause);
        notificationManager.notify(NOTIFICATE_ID,playNotification);
    }

    public class PlayBinder extends Binder {

        public PlayService getService() {
            return PlayService.this;
        }

    }

    //播放新的音乐带url
    public void playMusic(Music music){
        musicList.add(0,music);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(music.getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(playNotification==null){
            NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
            remoteViews=new RemoteViews(getPackageName(), R.layout.notification_view);
            Intent play_intent=new Intent("NOTIFICATION_PLAY");
            play_intent.putExtra("type",1);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,play_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.play,pendingIntent);
            Intent close_intent=new Intent("NOTIFICATION_PLAY");
            close_intent.putExtra("type",2);
            PendingIntent close_pendingIntent=PendingIntent.getBroadcast(this,2,close_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.close,close_pendingIntent);
            Intent next_intent=new Intent("NOTIFICATION_PLAY");
            next_intent.putExtra("type",3);
            PendingIntent next_pendingIntent=PendingIntent.getBroadcast(this,3,next_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.next,next_pendingIntent);
            Intent prev_intent=new Intent("NOTIFICATION_PLAY");
            prev_intent.putExtra("type",4);
            PendingIntent prev_pendingIntent=PendingIntent.getBroadcast(this,4,prev_intent,PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.iv_prev,prev_pendingIntent);

            builder.setSmallIcon(R.drawable.logo);
            playNotification = builder.build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                playNotification.bigContentView = remoteViews;
            }

            //playNotification.contentView=remoteViews;
        }
        remoteViews.setTextViewText(R.id.title,music.getTitle());
        remoteViews.setTextViewText(R.id.singer,music.getArtist()+"-"+music.getAlbum());
        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play_pause);
        notificationManager.notify(NOTIFICATE_ID,playNotification);

    }

    //暂停音乐
    public void pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            int duration=mediaPlayer.getDuration();
            System.out.println("duration的值为"+duration);
            System.out.println("执行了暂停函数");
            remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play_play);
            notificationManager.notify(NOTIFICATE_ID,playNotification);
        }
    }
    //暂停后重启播放音乐
    public void play(){
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play_pause);
        notificationManager.notify(NOTIFICATE_ID,playNotification);
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
    //返回场景信息
    public String getLocationDescri(){
        return myListener.getPoi_position();
    }
    //添加music到播放列表
    public void addMusic(Music music){
        musicList.add(0,music);
        System.out.println("musicList的长度为"+musicList.size());
        songPosition=-1;
    }
    //返回歌曲列表
    public List<Music> getMusicList(){
        return musicList;
    }

    //返回正在播放的歌曲
    public Music getMusic(){
        return musicList.get(songPosition);
    }

    public int getSongPosition() {
        return songPosition;
    }

    public void setSongPosition(int songPosition) {
        this.songPosition = songPosition;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("执行了PlayService的onDestroy方法");
    }
    class PlayServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("PlayService收到了广播"+intent.getAction());
            String recceiver_action=intent.getAction();
            if(recceiver_action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                Intent updatePlayer=new Intent("UPDATE_PLAYER");
                updatePlayer.putExtra("type",2);
                sendBroadcast(updatePlayer);
            }else if(recceiver_action.equals("NOTIFICATION_PLAY")){
                int type=intent.getIntExtra("type",0);//1表示播放暂停按钮，2表示关闭,3表示下一首,4表示上一首
                switch (type){
                    case 1:
                        if(mediaPlayer.isPlaying()) {
                            Intent updatePlayer = new Intent("UPDATE_PLAYER");
                            updatePlayer.putExtra("type", 2);
                            sendBroadcast(updatePlayer);
                        }else{
                            Intent updatePlayer = new Intent("UPDATE_PLAYER");
                            updatePlayer.putExtra("type", 3);
                            sendBroadcast(updatePlayer);
                        }
                        break;
                    case 2:
                        notificationManager.cancel(NOTIFICATE_ID);
                        Intent updatePlayer = new Intent("UPDATE_PLAYER");
                        updatePlayer.putExtra("type", 2);
                        sendBroadcast(updatePlayer);
                        playNotification=null;
                        break;
                    case 3:
                        next();
                        break;
                    case 4:
                        playPrev();
                        break;
                }

            }
        }
    }
}
