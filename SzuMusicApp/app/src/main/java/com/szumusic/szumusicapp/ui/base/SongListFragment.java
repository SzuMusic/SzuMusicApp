package com.szumusic.szumusicapp.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.common.SongListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SongListFragment extends Fragment {

    //是否可见
    protected boolean isVisble;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;
    private RecyclerView recyclerView;
    private SongListAdapter songListAdapter;
    UpdateSongListReceiver updateSongListReceiver;
    private List<Music> musicList=new ArrayList<Music>();
    Handler handler = new Handler();
    int time;
    int address;
    int weather;
    int mood;
    int state;
    String userid;

    public SongListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter("UPDATE_COMMEND");
        updateSongListReceiver=new UpdateSongListReceiver();
        getContext().registerReceiver(updateSongListReceiver,intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("进入了songlistFragment的onCreateview函数");
        View view=inflater.inflate(R.layout.fragment_song_list, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        songListAdapter=new SongListAdapter(getContext(),musicList);
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

    class UpdateSongListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            int type=intent.getIntExtra("type",1);//1表示收到用户主动修改场景信息的广播，2表示收到用户对某一歌曲评分的广播
            System.out.println("UpdateSongListReceiver收到了通知");
            switch (type){
                case 1:
                    time=intent.getIntExtra("time",0);
                    weather=intent.getIntExtra("weather",0);
                    address=intent.getIntExtra("address",0);
                    mood=intent.getIntExtra("mood",0);
                    state=intent.getIntExtra("state",0);
                    System.out.println(time);
                    String url="http://172.31.69.182:8080/MusicGrade/pRecMusic";
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("time",time);
                    map.put("weather",weather);
                    map.put("address",address);
                    map.put("mood",mood);
                    map.put("state",state);
                    map.put("userid",userid);
                    final JSONObject jsonObject = new JSONObject(map);
                    System.out.println(jsonObject.toString());
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder()
                              .add("data", jsonObject.toString())
                              .build();
                    Request request = new Request.Builder().url(url).post(formBody).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("请求");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = new String(response.body().string());
                            try {
                                JSONObject myjson = new JSONObject(result);
                                JSONArray musics=myjson.getJSONArray("musics");
                                JSONObject musicobj;
                                String[] str;
                                musicList.clear();
                                for (int i=0;i<musics.length();i++){
                                    musicobj=musics.getJSONObject(i);
                                    str=musicobj.getString("name").split("-");
                                    Music music=new Music();
                                    music.setTitle(str[1]);
                                    music.setArtist(str[0]);
                                    music.setAlbum(musicobj.getString("album"));
                                    music.setUri("http://120.27.106.28/musicSource/music/"+musicobj.getString("singerId")+"/"+musicobj.getString("musicId")+".mp3");
                                    music.setId(musicobj.getLong("musicId"));
                                    music.setProbability(musicobj.getDouble("probability"));
                                    musicList.add(music);
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        songListAdapter.notifyItemRangeChanged(0,musicList.size());
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case 2:
                    String url2="http://172.31.69.182:8080/MusicGrade/pGiveGrade";
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("time",time);
                    map2.put("weather",weather);
                    map2.put("address",address);
                    map2.put("mood",mood);
                    map2.put("state",state);
                    map2.put("userid",userid);
                    map2.put("musicId",intent.getLongExtra("musicId",0));
                    map2.put("score",intent.getIntExtra("score",0));
                    map2.put("probability",intent.getDoubleExtra("probability",0.5));
                    final JSONObject jsonObject2 = new JSONObject(map2);
                    System.out.println(jsonObject2.toString());
                    OkHttpClient client2 = new OkHttpClient();
                    FormBody formBody2 = new FormBody.Builder()
                            .add("data", jsonObject2.toString())
                            .build();
                    Request request2 = new Request.Builder().url(url2).post(formBody2).build();
                    client2.newCall(request2).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println(response.body().string());
                            Looper.prepare();
                            Toast.makeText(getActivity().getApplicationContext(),"评分成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }

        }
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
}
