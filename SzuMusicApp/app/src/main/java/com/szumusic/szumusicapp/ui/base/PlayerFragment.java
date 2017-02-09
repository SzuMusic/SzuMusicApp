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
import android.widget.LinearLayout;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.widget.IndicatorLayout;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ScreenUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import java.util.List;


public class PlayerFragment extends Fragment {

    @Bind(R.id.player_content)
    private LinearLayout player_content;
    @Bind(R.id.vp_play_page)
    private ViewPager vpPlay;
    @Bind(R.id.il_indicator)
    private IndicatorLayout ilIndicator;

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
        initSystemBar();
        initViewPager();
    }

    private void initViewPager() {
        View coverView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_player_album, null);
        View lrcView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_page_lrc, null);

    }
}
