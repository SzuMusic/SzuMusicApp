package com.szumusic.szumusicapp.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.common.SongListAdapter;
import com.szumusic.szumusicapp.ui.main.LocalActivity;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ViewBinder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;


public class HomeFragment extends Fragment implements View.OnClickListener {


    private Banner banner;
    @Bind(R.id.local_music)
    private ImageView local_music;
    String[] images= new String[] {"https://y.gtimg.cn/music/common/upload/t_focus_info_iphone/67296.jpg","https://y.gtimg.cn/music/common/upload/t_focus_info_iphone/66665.jpeg","https://y.gtimg.cn/music/common/upload/t_focus_info_iphone/67887.jpg"};
    private String name="无音乐";//歌名
    private String singer;//歌手
    private boolean isPlaying;//是否处于播放状态
    private UpdateReceiver updateReceiver;
    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter("UPDATE_PLAYER");
        updateReceiver=new UpdateReceiver();
        getContext().registerReceiver(updateReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        banner = (Banner) view.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setDelayTime(4000);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                System.out.println("点击了第"+position+"张图片");
            }
        });
        banner.setImages(images);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewBinder.bind(this,view);
        local_music.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.local_music:
                Intent intent=new Intent(getContext(), LocalActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("singer",singer);
                intent.putExtra("isPlaying",isPlaying);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(updateReceiver);
    }

    class UpdateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("HomeFragment也接受到了广播");
            int type=intent.getIntExtra("type",1);//1表示添加新的歌曲到播放列表。2表示播放暂停事件
            switch (type){
                case 1:
                    name=intent.getStringExtra("name");
                    singer=intent.getStringExtra("singer");
                    isPlaying=true;
                    break;
                case 2:
                    isPlaying=false;
                    break;
                case 3:
                    isPlaying=true;
                    break;
            }
        }
    }
}
