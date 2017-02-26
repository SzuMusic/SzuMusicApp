package com.szumusic.szumusicapp.ui.common;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;

import java.io.IOException;
import java.util.List;

/**
 * Created by kobe_xuan on 2017/2/11.
 */
public class LocalListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Music> musicList;//歌曲列表
    TextView songname, artist;


    public LocalListAdapter() {
        super();
    }

    public LocalListAdapter(Context context, List<Music> musicList) {
        super();
        this.context = context;
        this.musicList = musicList;
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_singer;
        TextView add_song;
        LinearLayout content_layout;

        public Viewholder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_singer = (TextView) itemView.findViewById(R.id.tv_singer);
            add_song= (TextView) itemView.findViewById(R.id.add_song);
            content_layout= (LinearLayout) itemView.findViewById(R.id.content_layout);
            content_layout.setOnClickListener(this);
            add_song.setOnClickListener(this);

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

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()){
                case R.id.content_layout:
                    Music current_music=musicList.get(position);
                    songname = (TextView) view.findViewById(R.id.tv_name);
                    artist = (TextView) view.findViewById(R.id.tv_singer);
                    System.out.println("点击了第" + position + "首歌曲");
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("name",songname.getText());
                    intent.putExtra("singer",artist.getText());
                    intent.putExtra("url",current_music.getUri());
                    intent.putExtra("total",current_music.getDuration());
                    intent.putExtra("type",1);
                    intent.putExtra("music",current_music);
                    context.sendBroadcast(intent);
                    break;
                case R.id.add_song:
                    System.out.println("点击了添加歌曲到列表的按钮");
                    Intent intent1=new Intent("UPDATE_PLAYER");
                    intent1.putExtra("type",6);
                    intent1.putExtra("music",musicList.get(position));
                    Toast.makeText(context,"成功添加一首歌曲",Toast.LENGTH_SHORT).show();
                    context.sendBroadcast(intent1);
                    break;
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.local_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        Music music = musicList.get(position);
        viewholder.getTv_name().setText(music.getTitle());
        viewholder.getTv_singer().setText(music.getArtist() + "-" + music.getAlbum());
        int minute= (int) (music.getDuration()/60000);
        int second=(int) (music.getDuration()%60000)/1000;
        System.out.println("第" + position + "首歌的路径是" + music.getUri()+"    时间为:   "+minute+"分"+second+"秒");
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }
}
