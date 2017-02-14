package com.szumusic.szumusicapp.ui.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.szumusic.szumusicapp.ui.base.FriendsFragment;
import com.szumusic.szumusicapp.ui.base.HomeFragment;
import com.szumusic.szumusicapp.ui.base.SongListFragment;
import com.szumusic.szumusicapp.ui.base.TransceiverFragment;
import com.szumusic.szumusicapp.ui.local.filesystem.LocalAlbumFragment;
import com.szumusic.szumusicapp.ui.local.filesystem.LocalDirectoryFragment;
import com.szumusic.szumusicapp.ui.local.filesystem.LocalSingerFragment;
import com.szumusic.szumusicapp.ui.local.filesystem.LocalSongsFragment;

/**
 * Created by kobe_xuan on 2017/2/10.
 */
public class LocalFragmentPagerAdapter extends FragmentPagerAdapter {
    public final int COUNT = 4;
    private String[] titles = new String[]{"单曲", "歌手", "专辑", "文件夹","777"};
    private Context context;

    public LocalFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                LocalSongsFragment tab1=new LocalSongsFragment();
                return tab1;
            case 1:
                LocalSingerFragment tab2=new LocalSingerFragment();
                return tab2;
            case 2:
                LocalAlbumFragment tab3=new LocalAlbumFragment();
                return tab3;
            case 3:
                LocalDirectoryFragment tab4=new LocalDirectoryFragment();
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
}
