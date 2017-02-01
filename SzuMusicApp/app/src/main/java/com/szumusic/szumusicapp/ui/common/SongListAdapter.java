package com.szumusic.szumusicapp.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;

/**
 * Created by kobe_xuan on 2017/1/28.
 */
public class SongListAdapter extends RecyclerView.Adapter {
    private int songcount=0;//歌曲的数量
    private Context context;

    public SongListAdapter() {
        super();
    }

    public SongListAdapter(Context context,int songcount) {
        super();
        this.context=context;
        this.songcount=songcount;
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView order_number;
        public Viewholder(View itemView) {
            super(itemView);
            order_number=(TextView)itemView.findViewById(R.id.order_number);
        }

        public TextView getOrder_number(){return order_number;}


    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        viewholder.getOrder_number().setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return songcount;
    }
}
