package com.szumusic.szumusicapp.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.base.PlayerFragment;
import com.szumusic.szumusicapp.ui.common.LocalFragmentPagerAdapter;
import com.szumusic.szumusicapp.ui.common.MyFragmentPagerAdapter;
import com.szumusic.szumusicapp.ui.music.player.PlayService;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.MusicUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Float.parseFloat;

public class LocalActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private List<Music> musicList;
    private MusicUtils musicUtils;
    PlayberUpdateReceiver playberUpdateReceiver;
    @Bind(R.id.tv_play_bar_title)
    TextView tv_play_bar_title;
    @Bind(R.id.tv_play_bar_artist)
    TextView tv_play_bar_artist;
    @Bind(R.id.iv_play_bar_play)
    ImageView iv_play_bar_play;
    @Bind(R.id.fl_play_bar)
    FrameLayout fl_play_bar;
    private PlayService playService;

    private PlayerFragment playerFragment;
    private boolean isPlayFragmentShow = false;//判断播放器的fragment是否正在显示


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        init();
    }

    private void init(){
        ViewBinder.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
       /* Intent intent=getIntent();
        tv_play_bar_title.setText(intent.getStringExtra("name"));
        tv_play_bar_artist.setText(intent.getStringExtra("singer"));
        iv_play_bar_play.setSelected(intent.getBooleanExtra("isPlaying",false));*/
        //下面是初始化控件部分
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_view);
        LocalFragmentPagerAdapter adapter = new LocalFragmentPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        iv_play_bar_play.setOnClickListener(this);
        fl_play_bar.setOnClickListener(this);

        PermissionGen.needPermission(LocalActivity.this, 100,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );

        //异步执行后台初始化
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                System.out.println("执行了LocalActivitydoInBackground函数");
                //启动服务
                Intent serviceIntent=new Intent(LocalActivity.this, PlayService.class);
                bindService(serviceIntent,LocalActivity.this,BIND_AUTO_CREATE);
                //注册音乐播放的广播接收器广播接收器
                IntentFilter intentFilter=new IntentFilter("UPDATE_PLAYER");
                playberUpdateReceiver=new PlayberUpdateReceiver();
                registerReceiver(playberUpdateReceiver,intentFilter);
                return null;
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

            }
        }.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
        System.out.println("请求权限成功");
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
        Looper.prepare();
        System.out.println("请求权限失败");
        Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playberUpdateReceiver);
        unbindService(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_play_bar_play:
                System.out.println("点击了播放暂停按钮");
                //判断当前是播放还是暂停
                if (iv_play_bar_play.isSelected()){
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("type",2);
                     sendBroadcast(intent);
                    iv_play_bar_play.setSelected(false);
                }else{
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("type",3);
                    sendBroadcast(intent);
                    iv_play_bar_play.setSelected(true);
                }
                break;
            case R.id.fl_play_bar:
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_slide_up,0);
                if(playerFragment==null){
                    playerFragment=new PlayerFragment();
                    ft.replace(R.id.mainHome, playerFragment);
                    System.out.println("进入了创建fragment的函数");
                }
                else{
                    ft.show(playerFragment);
                    System.out.println("playfragment不为空");
                }
                playerFragment.setTitle((String) tv_play_bar_title.getText());
                playerFragment.setSinger((String) tv_play_bar_artist.getText());
                playerFragment.setTotal(playService.getDuration());
                System.out.println("从service获得的进度为"+playService.getCurrentDuration());
                playerFragment.setCurrent_duration(playService.getCurrentDuration());
                playerFragment.setIsplaying(playService.getState());
                ft.commit();
                isPlayFragmentShow=true;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(playerFragment!=null&& isPlayFragmentShow==true){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(0, R.anim.fragment_slide_down);
            ft.hide(playerFragment);
            ft.commit();
            isPlayFragmentShow = false;
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        playService=( (PlayService.PlayBinder) iBinder).getService();
        if(playService.getMusicList().size()!=0){
            Music music=playService.getMusic();
            tv_play_bar_title.setText(music.getTitle());
            tv_play_bar_artist.setText(music.getArtist()+"-"+music.getAlbum());
            iv_play_bar_play.setSelected(playService.getState());
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        unbindService(this);
    }

    class PlayberUpdateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("LocalActivity接受到了广播");
            int type=intent.getIntExtra("type",1);//1表示添加新的歌曲到播放列表。2表示播放暂停事件
            switch (type){
                case 1:
                    String name=intent.getStringExtra("name");
                    String singer=intent.getStringExtra("singer");
                    tv_play_bar_title.setText(name);
                    tv_play_bar_artist.setText(singer);
                    iv_play_bar_play.setSelected(true);
                   /* if (playerFragment!=null)
                        playerFragment.setTitle(name);*/
                    break;
                case 2:
                    iv_play_bar_play.setSelected(false);
                    break;
                case 3:
                    iv_play_bar_play.setSelected(true);
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("HomeActivity收到type=5的广播");
                    String songname=intent.getStringExtra("name");
                    String songsinger=intent.getStringExtra("singer");
                    tv_play_bar_title.setText(songname);
                    tv_play_bar_artist.setText(songsinger);
                    iv_play_bar_play.setSelected(true);
                    break;
            }

        }
    }
}
