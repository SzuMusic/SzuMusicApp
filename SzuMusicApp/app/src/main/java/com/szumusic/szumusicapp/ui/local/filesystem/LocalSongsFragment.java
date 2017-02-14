package com.szumusic.szumusicapp.ui.local.filesystem;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.common.LocalListAdapter;
import com.szumusic.szumusicapp.ui.common.SongListAdapter;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.MusicUtils;
import com.szumusic.szumusicapp.utils.ViewBinder;

import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;


public class LocalSongsFragment extends Fragment {
    private List<Music> musicList=new ArrayList<>();
    private MusicUtils musicUtils=new MusicUtils();
    LocalListAdapter songListAdapter;
    RecyclerView recyclerView;
    boolean isScan=false;//判断是否扫描过了
    public LocalSongsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //readData();
        View view=inflater.inflate(R.layout.fragment_local_songs, container, false);
        System.out.println("进入了LocalSongsFragmentde的onCreateView函数");
        //readData();
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        if(!isScan){
            readData();
            isScan=true;
        }else
            recyclerView.setAdapter(songListAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println("执行了LocalSongsFragmentde的onViewCreated函数");
    }

    private void readData(){
        new AsyncTask<String, Void, List<Music>>() {
            @Override
            protected List<Music> doInBackground(String... params) {
                System.out.println("执行了LocalSongsFragmentdoInBackground函数");
                musicUtils.scanMusic(getContext(),musicList);
                return musicList;
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(List<Music> result) {
                super.onPostExecute(result);
                System.out.println("LocalSongsFragment里面的onPostExecute歌曲的数量为"+musicList.size());
                songListAdapter=new LocalListAdapter(getContext(),musicList);
                recyclerView.setAdapter(songListAdapter);
            }
        }.execute("kobe");
    }
}
