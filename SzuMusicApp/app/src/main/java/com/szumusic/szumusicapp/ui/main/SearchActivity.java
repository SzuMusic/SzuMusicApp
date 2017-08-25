package com.szumusic.szumusicapp.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;
import com.szumusic.szumusicapp.ui.base.SearchSongFragment;
import com.szumusic.szumusicapp.ui.common.SearchFragmentPagerAdapter;
import com.szumusic.szumusicapp.ui.music.player.PlayService;
import com.szumusic.szumusicapp.utils.Bind;
import com.szumusic.szumusicapp.utils.ViewBinder;

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

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.vp_view)
    ViewPager vp_view;
    @Bind(R.id.search_edit)
    EditText search_edit;
    SearchSongFragment searchSongFragment;
    String key;
    private List<Music> musicList=new ArrayList<Music>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

    }

    private void init(){
        ViewBinder.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

       Intent intent=getIntent();
        key=intent.getStringExtra("key");
        System.out.println("key为"+key);
        //下面是各控件的初始化部分
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SearchFragmentPagerAdapter searchFragmentPagerAdapter=new SearchFragmentPagerAdapter(getSupportFragmentManager(),this,musicList);
        vp_view.setAdapter(searchFragmentPagerAdapter);
        searchSongFragment= (SearchSongFragment) searchFragmentPagerAdapter.getItem(0);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp_view);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                System.out.println("进入了onEditorAction函数");
                if (i == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    System.out.println("监听到了回车事件");
                    return true;
                }
                return false;
            }
        });
        search_edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    System.out.println("监听到了回车");
                    return true;
                }
                return false;
            }
        });

        //异步执行后台初始化
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                System.out.println("执行了SearchActivity的asyncTask函数");
                String url="http://172.29.108.242:8080/MusicGrade/pSearchMusics";
                OkHttpClient client = new OkHttpClient();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("findKey",key);

                final JSONObject jsonObject = new JSONObject(map);
                FormBody formBody = new FormBody.Builder()
                        .add("data",jsonObject.toString())
                        .build();
                Request request = new Request.Builder().url(url).post(formBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                         String result=response.body().string();
                        System.out.println(result);
                        try {
                            JSONObject myjson=new JSONObject(result);
                            JSONArray musics=myjson.getJSONArray("musics");
                            JSONObject musicobj;
                            String[] str;
                            for (int i=0;i<musics.length();i++){
                                musicobj=musics.getJSONObject(i);
                                str=musicobj.getString("name").split("-");
                                Music music=new Music();
                                music.setTitle(str[1]);
                                music.setArtist(str[0]);
                                music.setCoverUri(musicobj.getString("imageUrl"));
                                music.setAlbum(musicobj.getString("album"));
                                music.setUri("https://www.szumusic.top/musicSource/music/"+musicobj.getString("singerId")+"/"+musicobj.getString("musicId")+".mp3");
                                music.setId(musicobj.getLong("musicId"));
                                System.out.println("获得的歌曲的url为："+"https://www.szumusic.top/musicSource/music/"+musicobj.getString("singerId")+"/"+musicobj.getString("musicId")+".mp3");
                                music.setProbability(musicobj.getDouble("probability"));
                                musicList.add(music);
                            }
                            searchSongFragment.setMusicList(musicList);
                            searchSongFragment.notifyChange();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
