package com.szumusic.szumusicapp.ui.base;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.common.SearchSongAdapter;
import com.szumusic.szumusicapp.ui.common.SongSheetAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchSongFragment extends Fragment {

    RecyclerView recyclerView;
    private List<Music> musicList=new ArrayList<Music>();
    SearchSongAdapter searchSongAdapter=new SearchSongAdapter(getContext(),musicList);
    Handler handler = new Handler();
    public SearchSongFragment() {

    }

    public static SearchSongFragment newInstance(String param1, String param2) {
        SearchSongFragment fragment = new SearchSongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search_song, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        searchSongAdapter=new SearchSongAdapter(getContext(),musicList);
        System.out.println("onCreateView中musicList的大小为"+musicList.size());
        recyclerView.setAdapter(searchSongAdapter);
        return view;
    }
    //增加歌曲
    public void addSong(Music music){
        musicList.add(music);
    }
    //通知增加
    public void notifyChange(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("musicList的列表大小为:"+musicList.size());
                searchSongAdapter.notifyItemRangeChanged(0,musicList.size());
            }
        });
    }

    public void setMusicList(List<Music> musicList){
        this.musicList=musicList;
    }




}
