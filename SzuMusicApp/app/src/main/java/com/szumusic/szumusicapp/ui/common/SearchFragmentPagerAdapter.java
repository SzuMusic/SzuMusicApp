package com.szumusic.szumusicapp.ui.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.base.FriendsFragment;
import com.szumusic.szumusicapp.ui.base.HomeFragment;
import com.szumusic.szumusicapp.ui.base.SearchAlbumFragment;
import com.szumusic.szumusicapp.ui.base.SearchSingerFragment;
import com.szumusic.szumusicapp.ui.base.SearchSongFragment;
import com.szumusic.szumusicapp.ui.base.SearchUserFragment;
import com.szumusic.szumusicapp.ui.base.SongListFragment;
import com.szumusic.szumusicapp.ui.base.TransceiverFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobe_xuan on 2017/2/25.
 */
public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"歌曲", "歌手", "专辑", "用户"};
    private Context context;
    SearchSongFragment tab1;

    public SearchFragmentPagerAdapter(FragmentManager fm, Context context,List<Music> musicList) {
        super(fm);
        this.context = context;
        tab1=new SearchSongFragment();
        tab1.setMusicList(musicList);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return tab1;
            case 1:
                SearchSingerFragment tab2=new SearchSingerFragment();
                return tab2;
            case 2:
                SearchAlbumFragment tab3=new SearchAlbumFragment();
                return tab3;
            case 3:
                SearchUserFragment tab4=new SearchUserFragment();
                return tab4;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
