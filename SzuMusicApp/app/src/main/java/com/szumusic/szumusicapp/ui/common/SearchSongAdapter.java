package com.szumusic.szumusicapp.ui.common;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobe_xuan on 2017/2/25.
 */
public class SearchSongAdapter extends RecyclerView.Adapter {
    private List<Music> musicList=new ArrayList<Music>();//播放歌曲列表

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Viewholder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
