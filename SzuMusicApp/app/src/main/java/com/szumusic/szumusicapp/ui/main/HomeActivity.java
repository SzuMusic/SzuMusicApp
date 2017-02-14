package com.szumusic.szumusicapp.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.base.PlayerFragment;
import com.szumusic.szumusicapp.ui.common.MyFragmentPagerAdapter;
import com.szumusic.szumusicapp.ui.music.player.PlayService;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ScreenUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private FrameLayout fl_play_bar;
    private PlayerFragment playerFragment;
    private boolean isPlayFragmentShow = false;//判断播放器的fragment是否正在显示
    private PlayService playService;
    PlayberUpdateReceiver playberUpdateReceiver;
    @Bind(R.id.tv_play_bar_title)
    TextView tv_play_bar_title;
    @Bind(R.id.tv_play_bar_artist)
    TextView tv_play_bar_artist;
    @Bind(R.id.iv_play_bar_play)
    ImageView iv_play_bar_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        init();


    }

    private void init() {
        ViewBinder.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_view);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                this);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        fl_play_bar=(FrameLayout)findViewById(R.id.fl_play_bar);
        fl_play_bar.setOnClickListener(this);
//        传入当前activity的context
        ScreenUtils.init(this);
        //异步执行后台初始化
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                System.out.println("执行了HomeActivitydoInBackground函数");
                //启动服务
                Intent serviceIntent=new Intent(HomeActivity.this, PlayService.class);
                bindService(serviceIntent,HomeActivity.this,BIND_AUTO_CREATE);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
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

   //onClick事件处理
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fl_play_bar:
                System.out.println("点击了播放器");
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_slide_up,0);
                if(playerFragment==null){
                    playerFragment=new PlayerFragment();
                    ft.replace(R.id.mainHome, playerFragment);
                }
                else{
                    ft.show(playerFragment);
                    System.out.println("playfragment不为空");
                }
                ft.commit();
                isPlayFragmentShow=true;
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        System.out.println("执行了onServiceConnected函数");
        playService=( (PlayService.PlayBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public PlayService getPlayService() {
        return playService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playberUpdateReceiver);
        unbindService(this);
    }

    class PlayberUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("HomeActivity接受到了广播");
            int type=intent.getIntExtra("type",1);//1表示添加新的歌曲到播放列表。2表示播放暂停事件
            switch (type){
                case 1:
                    String name=intent.getStringExtra("name");
                    String singer=intent.getStringExtra("singer");
                    String url=intent.getStringExtra("url");
                    playService.playMusic(url);
                    tv_play_bar_title.setText(name);
                    tv_play_bar_artist.setText(singer);
                    iv_play_bar_play.setSelected(true);
                    break;
                case 2:
                    playService.pause();
                    break;
                case 3:
                    playService.play();
                    break;
            }
        }
    }

}
