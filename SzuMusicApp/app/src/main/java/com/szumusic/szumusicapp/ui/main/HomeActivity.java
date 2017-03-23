package com.szumusic.szumusicapp.ui.main;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.base.PlayerFragment;
import com.szumusic.szumusicapp.ui.common.MyFragmentPagerAdapter;
import com.szumusic.szumusicapp.ui.common.SettingListAdapter;
import com.szumusic.szumusicapp.ui.music.player.PlayService;
import com.szumusic.szumusicapp.utils.ActivityManagerApplication;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ImageUtils;
import com.szumusic.szumusicapp.utils.ScreenUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    private LinearLayout player_layout;
    private PlayerFragment playerFragment;
    private boolean isPlayFragmentShow = false;//判断播放器的fragment是否正在显示
    private PlayService playService;
    PlayberUpdateReceiver playberUpdateReceiver;
    AlertDialog dialog=null;//场景定位的dialog
    AlertDialog userinfo_dialog=null;
    @Bind(R.id.tv_play_bar_title)
    TextView tv_play_bar_title;
    @Bind(R.id.tv_play_bar_artist)
    TextView tv_play_bar_artist;
    @Bind(R.id.iv_play_bar_play)
    ImageView iv_play_bar_play;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.talk_icon)
    ImageView talk_icon;
    @Bind(R.id.search_edit)
    EditText search_edit;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.iv_play_bar_cover)
    ImageView iv_play_bar_cover;
    @Bind(R.id.user_photo)
    ImageView user_photo;
    MaterialSpinner spinner_time;
    MaterialSpinner spinner_position;
    MaterialSpinner spinner_weather;
    MaterialSpinner spinner_state;
    MaterialSpinner spinner_mood;

    MaterialSpinner spinner_age;
    MaterialSpinner spinner_sex;
    TextView tv_position;//用户定位
    TextView change_msg;
    TextView update_userinfo;
    TextView cancel_update;
    EditText e_name_tv;
    public static String user_id;
    private String e_name;
    private SharedPreferences sp;
    private Handler handler=new Handler();
    Bitmap coverBg;
    Handler handlerImg=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Bitmap bmp=(Bitmap)msg.obj;
                    iv_play_bar_cover.setImageBitmap(bmp);
                    coverBg= ImageUtils.blur(bmp,ImageUtils.BLUR_RADIUS);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManagerApplication.destoryActivity("login");
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
        sp=this.getSharedPreferences("userinfo",MODE_PRIVATE);
        user_id=sp.getString("user_id",null);
        e_name=sp.getString("e_name",null);
        user_name.setText(e_name);
        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_view);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),
                this);
        adapter.setUserid(user_id);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        SettingListAdapter settingListAdapter=new SettingListAdapter(this);//如果等下写事件的时候有错，改用全局的applicationcontext,一般别滥用全局的上下文，不然很容易bug
        recyclerView.setAdapter(settingListAdapter);

        player_layout=(LinearLayout) findViewById(R.id.player_layout);
        player_layout.setOnClickListener(this);
        iv_play_bar_play.setOnClickListener(this);
        fab.setOnClickListener(this);
        user_photo.setOnClickListener(this);
        talk_icon.setOnClickListener(this);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER))&&!search_edit.getText().equals("")) {
                    System.out.println("监听到了回车事件");
                    Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                    intent.putExtra("key",search_edit.getText().toString());
                    System.out.println("放进去的key 为"+search_edit.getText());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

//        传入当前activity的context
        ScreenUtils.init(this);
        //异步执行后台初始化
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                System.out.println("执行了HomeActivitydoInBackground函数");
                //启动服务
                Intent serviceIntent=new Intent(HomeActivity.this, PlayService.class);
                //startService(serviceIntent);
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
            case R.id.player_layout:
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
                playerFragment.setTitle((String) tv_play_bar_title.getText());
                playerFragment.setSinger((String) tv_play_bar_artist.getText());
                playerFragment.setTotal( playService.getDuration());
                System.out.println("从service获得的进度为"+playService.getCurrentDuration());
                playerFragment.setCurrent_duration( playService.getCurrentDuration());
                playerFragment.setIsplaying(playService.getState());
                if (coverBg!=null)
                    playerFragment.setBlackground(coverBg);
                ft.commit();
                isPlayFragmentShow=true;
                break;
            case R.id.iv_play_bar_play:
                System.out.println("点击了HomeActivity的播放暂停按钮");
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
            case R.id.fab:
                if(dialog!=null){
                    dialog.show();
                }else{
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View dialog_view = inflater.inflate(R.layout.dialog_position, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(dialog_view);
                    spinner_time= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_time);
                    spinner_position= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_position);
                    spinner_weather= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_weather);
                    spinner_state= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_state);
                    spinner_mood= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_mood);
                    tv_position= (TextView) dialog_view.findViewById(R.id.tv_position);
                    change_msg= (TextView) dialog_view.findViewById(R.id.change_msg);
                    change_msg.setOnClickListener(this);
                    spinner_time.setItems("清晨","上午","午间","下午","夜晚","深夜");
                    spinner_position.setItems("居室","办公室","商店","酒吧","街道","户外","图书馆","室内运功场");
                    spinner_weather.setItems("晴天","阴天","雨天");
                    spinner_state.setItems("正常","在工作","在学习","在休息","在路途","在劳务");
                    spinner_mood.setItems("悲伤","失落","正常","愉悦","兴奋");
                    tv_position.setText("       "+playService.getLocationDescri());
                    dialog=builder.create();
                    dialog.show();
                }
                break;
            case R.id.talk_icon:
                Toast.makeText(getApplicationContext(),"语音识别功能正在开发中，敬请关注",Toast.LENGTH_SHORT).show();
                System.out.println("点击了语音icon");
                break;
            case R.id.change_msg:
                Intent intent=new Intent("UPDATE_COMMEND");
                intent.putExtra("type",1);
                intent.putExtra("userid",user_id);
                intent.putExtra("time",spinner_time.getSelectedIndex()+1);
                intent.putExtra("address",spinner_position.getSelectedIndex()+1);
                intent.putExtra("weather",spinner_weather.getSelectedIndex()+1);
                intent.putExtra("mood",spinner_mood.getSelectedIndex()+1);
                intent.putExtra("state",spinner_state.getSelectedIndex()+1);
                sendBroadcast(intent);
                System.out.println("time选择的是"+spinner_time.getSelectedIndex()+spinner_time.getText());
                dialog.hide();
                break;
            case R.id.user_photo:
                if(userinfo_dialog!=null){
                    userinfo_dialog.show();
                }else{
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View dialog_view = inflater.inflate(R.layout.dialog_userinfo, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(dialog_view);
                    spinner_age= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_age);
                    spinner_sex= (MaterialSpinner) dialog_view.findViewById(R.id.spinner_sex);
                    e_name_tv= (EditText) dialog_view.findViewById(R.id.e_name);
                    e_name_tv.setText(e_name);
                    update_userinfo= (TextView) dialog_view.findViewById(R.id.update_userinfo);
                    update_userinfo.setOnClickListener(this);
                    cancel_update= (TextView) dialog_view.findViewById(R.id.cancel_update);
                    cancel_update.setOnClickListener(this);
                    spinner_age.setItems("00后","90后","80后","70后","60后","60前");
                    spinner_sex.setItems("保密", "男", "女");
                    userinfo_dialog=builder.create();
                    userinfo_dialog.show();
                }
                break;
            case R.id.update_userinfo:
                String url="http://172.31.69.182:8080/MusicGrade/pUpdUserInfo";
                OkHttpClient client = new OkHttpClient();
                Map<String, Object> map = new HashMap<String, Object>();
                e_name=e_name_tv.getText().toString();
                map.put("user_id",user_id);
                map.put("e_name",e_name_tv.getText().toString());
                map.put("age",spinner_age.getSelectedIndex());
                map.put("sex",spinner_sex.getSelectedIndex());
                System.out.println(spinner_age.getSelectedIndex()+"==="+spinner_age.getText());
                final JSONObject jsonObject = new JSONObject(map);
                FormBody formBody = new FormBody.Builder()
                        .add("data",jsonObject.toString())
                        .build();
                Request request = new Request.Builder().url(url).post(formBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result=response.body().string();
                        System.out.println(result);
                        try {
                            JSONObject myjson=new JSONObject(result);
                            Boolean isSucceed=myjson.getBoolean("flag");
                            if(isSucceed) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HomeActivity.this, "信息修改成功~", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    userinfo_dialog.hide();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.cancel_update:
                userinfo_dialog.hide();
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        playService=( (PlayService.PlayBinder) iBinder).getService();
        System.out.println("执行了onServiceConnected函数:当前mediaplayer的状态为" + playService.getState() + playService.getMusicList().size());
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

    public PlayService getPlayService() {
        return playService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playberUpdateReceiver);
        unbindService(this);
        System.out.println("进入了HomeActivity的destroy函数");
    }

    class PlayberUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",1);//1表示添加新的歌曲到播放列表。3、2表示播放暂停事件,4表示更新进度,5表示监听到播放完成后自动播放下一首,6表示增加歌曲到播放列表，7表示向service请求下一首
            System.out.println("HomeActivity接受到了广播type："+type);
            switch (type){
                case 1:
                    Music music= (Music) intent.getSerializableExtra("music");
                    playService.playMusic(music);
                    tv_play_bar_title.setText(music.getTitle());
                    tv_play_bar_artist.setText(music.getArtist()+"-"+music.getAlbum());
                    iv_play_bar_play.setSelected(true);
                    tv_play_bar_title.setText(music.getTitle());
                    tv_play_bar_artist.setText(music.getArtist()+"-"+music.getAlbum());
                    iv_play_bar_play.setSelected(true);
                    if(music.getCoverUri()!=null){
                        System.out.println("专辑封面url为："+music.getCoverUri());
                        OkHttpClient imgClient=new OkHttpClient();
                        Request imgRequest=new Request.Builder().url("https:"+music.getCoverUri()).build();
                        imgClient.newCall(imgRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                System.out.println("下载图片成功");
                                byte[] bytes = response.body().bytes();
                                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                Message msg=new Message();
                                msg.what=0;
                                msg.obj=bmp;
                                handlerImg.sendMessage(msg);

                            }
                        });
                    }
                    break;
                case 2:
                    playService.pause();
                    iv_play_bar_play.setSelected(false);
                    break;
                case 3:
                    playService.play();
                    iv_play_bar_play.setSelected(true);
                    break;
                case 4:
                    int progress=intent.getIntExtra("progress",0);
                    playService.setProgress(progress);
                    break;
                case 5:
                    String songname=intent.getStringExtra("name");
                    String songsinger=intent.getStringExtra("singer");
                    tv_play_bar_title.setText(songname);
                    tv_play_bar_artist.setText(songsinger);
                    iv_play_bar_play.setSelected(true);
                    break;
                case 6:
                    Music music1= (Music) intent.getSerializableExtra("music");
                    playService.addMusic(music1);
                    break;
                case 7:
                    System.out.println("HomeActivity调用了playService的next函数");
                    playService.next();
                    break;
            }
        }
    }

}
