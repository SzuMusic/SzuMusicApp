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
 * Created by kobe_xuan on 2017/2/21.
 */
public class SongSheetAdapter extends RecyclerView.Adapter {

    private int sheetcount =0;//歌曲的数量
    private Context context;
    private List<Music> musicList=new ArrayList<Music>();
    public SongSheetAdapter(){

    }

    public SongSheetAdapter(Context context,int songcount) {
        this.sheetcount = songcount;
        this.context = context;
    }
    public SongSheetAdapter(Context context,int songcount,List<Music> musicList) {
        this.sheetcount = songcount;
        this.context = context;
        this.musicList=musicList;
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView song_cover;
        TextView tv_name;
        TextView tv_singer;
        RelativeLayout content_layout;
        public Viewholder(View itemView) {
            super(itemView);
            song_cover= (ImageView) itemView.findViewById(R.id.song_cover);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_singer= (TextView) itemView.findViewById(R.id.tv_singer);
            content_layout= (RelativeLayout) itemView.findViewById(R.id.content_layout);
            content_layout.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()){
                case R.id.content_layout:
                    Toast.makeText(context,"正在播放歌曲",Toast.LENGTH_SHORT).show();
                    Music music=musicList.get(position);
                    Music music1=new Music();
                    music1.setUri(music.getUri());
                    music1.setTitle(music.getTitle());
                    music1.setArtist(music.getArtist());
                    music1.setAlbum(music1.getArtist());
                    music1.setCoverUri(music.getCoverUri());
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("name",music.getTitle());
                    intent.putExtra("singer",music.getArtist());
                    intent.putExtra("url",music.getUri());
                    intent.putExtra("type",1);
                    intent.putExtra("music",music1);
                    context.sendBroadcast(intent);
                    break;
            }
        }

        public ImageView getSong_cover() {
            return song_cover;
        }

        public void setSong_cover(ImageView song_cover) {
            this.song_cover = song_cover;
        }

        public TextView getTv_name() {
            return tv_name;
        }

        public void setTv_name(TextView tv_name) {
            this.tv_name = tv_name;
        }

        public TextView getTv_singer() {
            return tv_singer;
        }

        public void setTv_singer(TextView tv_singer) {
            this.tv_singer = tv_singer;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_sheet, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SongSheetAdapter.Viewholder viewholder = (SongSheetAdapter.Viewholder) holder;
       if(musicList!=null&&musicList.size()>0){
           Music music=musicList.get(position);
           viewholder.getTv_name().setText(music.getTitle());
           viewholder.getTv_singer().setText(music.getArtist());
        viewholder.getSong_cover().setImageBitmap(music.getCover());
       }
    }

    @Override
    public int getItemCount() {
        return sheetcount;
    }
}
