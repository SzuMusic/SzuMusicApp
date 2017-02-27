package com.szumusic.szumusicapp.ui.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.szumusic.szumusicapp.ui.base.FriendsFragment;
import com.szumusic.szumusicapp.ui.base.HomeFragment;
import com.szumusic.szumusicapp.ui.base.SongListFragment;
import com.szumusic.szumusicapp.ui.base.TransceiverFragment;

/**
 * Created by kobe_xuan on 2017/1/25.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public final int COUNT = 4;
    private String[] titles = new String[]{"首页", "歌单", "好友", "电台","777"};
    private Context context;

    private String userid;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                HomeFragment tab1=new HomeFragment();
                return tab1;
            case 1:
                SongListFragment tab2=new SongListFragment();
                tab2.setUserid(userid);
                return tab2;
            case 2:
                FriendsFragment tab3=new FriendsFragment();
                return tab3;
            case 3:
                TransceiverFragment tab4=new TransceiverFragment();
                return tab4;
        }
        return null;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
