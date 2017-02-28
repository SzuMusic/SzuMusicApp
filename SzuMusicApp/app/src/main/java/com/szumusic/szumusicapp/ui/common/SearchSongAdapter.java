package com.szumusic.szumusicapp.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobe_xuan on 2017/2/25.
 */
public class SearchSongAdapter extends RecyclerView.Adapter {
    private List<Music> musicList=new ArrayList<Music>();//播放歌曲列表
   Context context;
    public SearchSongAdapter(Context context,List<Music> musicList) {
        this.context=context;
        this.musicList = musicList;
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView singer;
        RelativeLayout content_layout;
        public TextView getTitle() {
            return title;
        }

        public TextView getSinger() {
            return singer;
        }

        public Viewholder(View view) {
            super(view);
            title= (TextView) view.findViewById(R.id.title);
            singer= (TextView) view.findViewById(R.id.singer);
            content_layout= (RelativeLayout) view.findViewById(R.id.content_layout);
            content_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
           switch (view.getId()){
               case R.id.content_layout:
                   Music music=musicList.get(position);
                   Intent intent=new Intent("UPDATE_PLAYER");
                   intent.putExtra("name",music.getTitle());
                   intent.putExtra("singer",music.getArtist());
                   intent.putExtra("url",music.getUri());
                   intent.putExtra("type",1);
                   intent.putExtra("music",music);
                   context.sendBroadcast(intent);
                   Toast.makeText(context,"正在播放歌曲",Toast.LENGTH_SHORT).show();
                   break;
           }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        Music music = musicList.get(position);
        viewholder.getTitle().setText(music.getTitle());
        viewholder.getSinger().setText(music.getArtist());

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }
}
