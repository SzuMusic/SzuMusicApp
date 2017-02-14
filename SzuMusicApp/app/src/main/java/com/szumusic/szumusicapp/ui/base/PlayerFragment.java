package com.szumusic.szumusicapp.ui.base;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.common.PlayPagerAdapter;
import com.szumusic.szumusicapp.ui.widget.IndicatorLayout;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ScreenUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import java.util.ArrayList;
import java.util.List;


public class PlayerFragment extends Fragment implements ViewPager.OnPageChangeListener {

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

    String title;//歌名
    String singer;//歌手
    Long total;//时间的总长度为
    Long current_duration;//当前的进度
    int total_minute;//分
    int total_second;//秒

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
        System.out.println("进入了初始化函数");
        //下面都是控件的初始化
        initSystemBar();
        initViewPager();
        ilIndicator.create(mViewPagerContent.size());
        tv_title.setText(title);
        iv_play.setSelected(true);
        tv_artist.setText(singer);
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
    public void setTotal(Long total){
        this.total=total;
    }
}
