package com.szumusic.szumusicapp.ui.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
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

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.base.PlayerFragment;
import com.szumusic.szumusicapp.ui.common.MyFragmentPagerAdapter;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ScreenUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private FrameLayout fl_play_bar;
    private PlayerFragment playerFragment;
    private boolean isPlayFragmentShow = false;//判断播放器的fragment是否正在显示

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
}
