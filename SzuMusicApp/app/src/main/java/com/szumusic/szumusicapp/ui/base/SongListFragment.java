package com.szumusic.szumusicapp.ui.base;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.common.SongListAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class SongListFragment extends Fragment {

    //是否可见
    protected boolean isVisble;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;
    private RecyclerView recyclerView;
    private SongListAdapter songListAdapter;
    public SongListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("进入了songlistFragment的onCreateview函数");
        View view=inflater.inflate(R.layout.fragment_song_list, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        songListAdapter=new SongListAdapter(getContext(),5);
        recyclerView.setAdapter(songListAdapter);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("进入了setUserVisibleHint函数"+getUserVisibleHint());
        if(!isPrepared){
            /*String url="";
            Map<String, Object> map = new HashMap<String, Object>();
            final JSONObject jsonObject = new JSONObject(map);
            System.out.println(jsonObject.toString());
            OkHttpClient client = new OkHttpClient();
            FormBody formBody = new FormBody.Builder()
                    .add("data", jsonObject.toString())
                    .build();
            Request request = new Request.Builder().url(url).post(formBody).build();*/
        }
    }
}
