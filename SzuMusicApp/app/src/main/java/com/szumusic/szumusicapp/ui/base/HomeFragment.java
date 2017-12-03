package com.szumusic.szumusicapp.ui.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.common.SongListAdapter;
import com.szumusic.szumusicapp.ui.common.SongSheetAdapter;
import com.szumusic.szumusicapp.ui.main.LocalActivity;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ViewBinder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

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


public class HomeFragment extends Fragment implements View.OnClickListener {


    private Banner banner;
    private ImageView local_music;
    String[] images= new String[] {"https://y.gtimg.cn/music/common/upload/t_focus_info_iphone/67296.jpg","https://y.gtimg.cn/music/common/upload/t_focus_info_iphone/66665.jpeg","https://y.gtimg.cn/music/common/upload/t_focus_info_iphone/67887.jpg"};
    private String name="无音乐";//歌名
    private String singer;//歌手
    private boolean isPlaying;//是否处于播放状态
    private UpdateReceiver updateReceiver;
    RecyclerView recyclerView;
    SongSheetAdapter songSheetAdapter;
    private List<Music> musicList=new ArrayList<Music>();//新歌速递列表
    Handler handler = new Handler();

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter("UPDATE_PLAYER");
        updateReceiver=new UpdateReceiver();
        getContext().registerReceiver(updateReceiver,intentFilter);
        //异步执行后台初始化
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                System.out.println("执行了HomeFragment函数");
                String url="https://www.szumusic.top/pFirstSongs";
                Map<String, Object> map = new HashMap<String, Object>();
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
                        System.out.println("请求pFirstSongs失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = new String(response.body().string());
                        System.out.println("获得的6首歌的数据");
                        System.out.println(result);
                        try {
                            JSONObject myjson= new JSONObject(result);
                            JSONArray musics=myjson.getJSONArray("musics");
                            for(int i=0;i<musics.length();i++){
                                JSONObject myobj= musics.getJSONObject(i);
                                final Music music=new Music();
                                music.setTitle(myobj.getString("name"));
                                music.setArtist(myobj.getString("singer"));
                                music.setUri(myobj.getString("songUrl"));
                                music.setCoverUri(myobj.getString("imageUrl"));
                                OkHttpClient imgClient=new OkHttpClient();
                                Request imgRequest=new Request.Builder().url("https:"+myobj.getString("imageUrl")).build();
                                imgClient.newCall(imgRequest).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        System.out.println("下载图片成功");
                                        byte[] bytes = response.body().bytes();
                                        Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                        music.setCover(bmp);
                                    }
                                });
                                musicList.add(music);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       songSheetAdapter.notifyItemRangeChanged(0,musicList.size());
                       System.out.println("执行了更新方法啦啦啦啦啦");
                   }
               });
                return null;
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        local_music= (ImageView) view.findViewById(R.id.local_music);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
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
        System.out.println("执行到了HomeFragment的onCreateView");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        songSheetAdapter=new SongSheetAdapter(getContext(),6,musicList);
        recyclerView.setAdapter(songSheetAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        local_music.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.local_music:
                Intent intent=new Intent(getContext(), LocalActivity.class);
              /*  intent.putExtra("name",name);
                intent.putExtra("singer",singer);
                intent.putExtra("isPlaying",isPlaying);*/
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
                case 4:
                    break;
                case 5:
                    name=intent.getStringExtra("name");
                    singer=intent.getStringExtra("singer");
                    isPlaying=true;
                    break;
            }
        }
    }
}
