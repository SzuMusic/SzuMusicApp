package com.szumusic.szumusicapp.ui.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.common.PlayPagerAdapter;
import com.szumusic.szumusicapp.ui.widget.IndicatorLayout;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ScreenUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlayerFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.player_content)
    private LinearLayout player_content;
    @Bind(R.id.vp_play_page)
    private ViewPager vpPlay;
    @Bind(R.id.il_indicator)
    private IndicatorLayout ilIndicator;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.iv_play)
    ImageView iv_play;
    @Bind(R.id.tv_artist)
    TextView tv_artist;
    @Bind(R.id.tv_total_time)
    TextView tv_total_time;
    @Bind(R.id.tv_current_time)
    TextView tv_current_time;
    @Bind(R.id.sb_progress)
    SeekBar sb_progress;

    String title;//歌名
    String singer;//歌手
    int total;//时间的总长度为
    int current_duration=0;//当前的进度
    int total_minute=0;//分
    int total_second=0;//秒
    int current_minute;//当前分
    int current_second;//当前面秒
    Handler handler=new Handler();
    Timer timer=new Timer();
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            current_duration+=1000;
            current_minute=(int)(current_duration/60000);
            current_second=(int)(current_duration%60000)/1000;
            if(current_second<10){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_current_time.setText("0"+current_minute+":0"+current_second);
                        double progress=(current_duration/(double)total)*100;
                        //System.out.println("当前的进度为"+(int)progress);
                        sb_progress.setProgress((int)progress);
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_current_time.setText("0"+current_minute+":"+current_second);
                        double progress=(current_duration/(double)total)*100;
                        //System.out.println("当前的进度为"+(int)progress);
                        sb_progress.setProgress((int)progress);
                    }
                });
            }

        }
    };
    boolean isPlaying=false;
    private List<View> mViewPagerContent;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_player, container, false);
        //player_content=(LinearLayout)view.findViewById(R.id.player_content);
        System.out.println("进入了解析函数");
        //tv_title=(TextView) view.findViewById(R.id.tv_title);
        current_minute=(int)(current_duration/60000);
        current_second=(int)(current_duration%60000)/1000;
        if(current_second<10){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_current_time.setText("0"+current_minute+":0"+current_second);
                    double progress=(current_duration/(double)total)*100;
                    //System.out.println("当前的进度为"+(int)progress);
                    sb_progress.setProgress((int)progress);
                }
            });
        }else{
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_current_time.setText("0"+current_minute+":"+current_second);
                    double progress=(current_duration/(double)total)*100;
                    //System.out.println("当前的进度为"+(int)progress);
                    sb_progress.setProgress((int)progress);
                }
            });
        }
        if(isPlaying)
            timer.schedule(timerTask,0,1000);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight();
            player_content.setPadding(0, top, 0, 0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewBinder.bind(this,view);
        System.out.println("进入了PlayerFragment的onViewCreated函数");
        //下面都是控件的初始化
        initSystemBar();
        initViewPager();
        ilIndicator.create(mViewPagerContent.size());
        iv_play.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(this);
        tv_title.setText(title);
        iv_play.setSelected(isPlaying);
        tv_artist.setText(singer);
        System.out.println(total);
        total_minute= (int) (total/60000);
        total_second= (int) (total%60000)/1000;
        if (total_second<10)
            tv_total_time.setText("0"+total_minute+":0"+total_second);
        else
            tv_total_time.setText("0"+total_minute+":"+total_second);


    }

    private void initViewPager() {
        View coverView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_player_album, null);
        View lrcView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_page_lrc, null);

        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        vpPlay.setAdapter(new PlayPagerAdapter(mViewPagerContent));
        vpPlay.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ilIndicator.setCurrent(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setTitle(String title){
        this.title=title;
    }
    public void setSinger(String singer){
        this.singer=singer;
    }
    public void setTotal(int total){
        this.total=total;
    }
    public void setCurrent_duration(int current_duration){
        this.current_duration=current_duration;
    }
    public void setIsplaying(boolean isPlaying){
        this.isPlaying=isPlaying;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("执行到了onHiddenChanged的函数");
        if(!hidden){
            tv_title.setText(title);
            iv_play.setSelected(true);
            tv_artist.setText(singer);
            total_minute= (int) (total/60000);
            total_second= (int) (total%60000)/1000;
            iv_play.setSelected(isPlaying);
            if (total_second<10)
                tv_total_time.setText("0"+total_minute+":0"+total_second);
            else
                tv_total_time.setText("0"+total_minute+":"+total_second);
            double progress=(current_duration/(double)total)*100;
            //System.out.println("当前的进度为"+(int)progress);
            sb_progress.setProgress((int)progress);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerTask.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_play:
                if(iv_play.isSelected()){
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("type",2);
                    getContext().sendBroadcast(intent);
                    iv_play.setSelected(false);
                    timerTask.cancel();
                    timer.cancel();
                }else{
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("type",3);
                    getContext().sendBroadcast(intent);
                    iv_play.setSelected(true);
                    timer=new Timer();
                    timerTask=new TimerTask() {
                        @Override
                        public void run() {
                            current_duration+=1000;
                            current_minute=(int)(current_duration/60000);
                            current_second=(int)(current_duration%60000)/1000;
                            if(current_second<10){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_current_time.setText("0"+current_minute+":0"+current_second);
                                        double progress=(current_duration/(double)total)*100;
                                        sb_progress.setProgress((int)progress);
                                    }
                                });
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_current_time.setText("0"+current_minute+":"+current_second);
                                        double progress=(current_duration/(double)total)*100;
                                        sb_progress.setProgress((int)progress);
                                    }
                                });
                            }

                        }
                    };
                    timer.schedule(timerTask,0,1000);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //System.out.println("播放进度发生了改变啦:"+i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        System.out.println("进入了onStopTrackingTouch函数"+seekBar.getProgress());
        int progress=seekBar.getProgress();
        current_duration=progress*total/100;
        Intent intent=new Intent("UPDATE_PLAYER");
        intent.putExtra("type",4);
        intent.putExtra("progress",progress*total/100);
        getContext().sendBroadcast(intent);
    }
}
