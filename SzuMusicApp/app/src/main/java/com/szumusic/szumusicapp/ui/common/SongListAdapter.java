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
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.data.model.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobe_xuan on 2017/1/28.
 */
public class SongListAdapter extends RecyclerView.Adapter {
    private Context context;
    Dialog markDialog=null;//评分的弹框
    private List<Music> musicList=new ArrayList<Music>();

    public SongListAdapter() {
        super();
    }

    public SongListAdapter(Context context,List<Music> musicList) {
        super();
        this.context=context;
        this.musicList=musicList;
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView order_number;
        ImageView btn_mark;
        LinearLayout content_layout;
        TextView tv_title;
        TextView tv_artist;
        public Viewholder(View itemView) {
            super(itemView);
            order_number=(TextView)itemView.findViewById(R.id.order_number);
            btn_mark= (ImageView) itemView.findViewById(R.id.btn_mark);
            content_layout= (LinearLayout) itemView.findViewById(R.id.content_layout);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist= (TextView) itemView.findViewById(R.id.tv_artist);
            btn_mark.setOnClickListener(this);
            order_number.setOnClickListener(this);
            content_layout.setOnClickListener(this);
        }

        public TextView getOrder_number(){return order_number;}
        public TextView getTv_title(){return tv_title;}
        public TextView getTv_artist(){return tv_artist;}


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId()){
                case R.id.btn_mark:
                    if(markDialog!=null)
                        markDialog.show();
                    else{
                        markDialog=new Dialog(context,R.style.my_dialog);
                        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                                R.layout.dialog_mark, null);
                        markDialog.setContentView(root);
                        Window dialogWindow = markDialog.getWindow();
                        dialogWindow.setGravity(Gravity.BOTTOM);
                        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
                        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                        lp.x = 0; // 新位置X坐标
                        lp.y = -20; // 新位置Y坐标
                        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
                        root.measure(0, 0);
                        lp.height = root.getMeasuredHeight();
                        lp.alpha = 9f; // 透明度
                        dialogWindow.setAttributes(lp);
                        markDialog.setCanceledOnTouchOutside(true);
                        markDialog.show();
                    }
                    break;
                case R.id.content_layout:
                    Music music=musicList.get(position);
                    Intent intent=new Intent("UPDATE_PLAYER");
                    intent.putExtra("name",music.getTitle());
                    intent.putExtra("singer",music.getArtist());
                    intent.putExtra("url",music.getUri());
                    intent.putExtra("type",1);
                    intent.putExtra("music",music);
                    context.sendBroadcast(intent);
                    break;


            }
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Viewholder viewholder = (Viewholder) holder;
        Music music=musicList.get(position);
        viewholder.getOrder_number().setText(Integer.toString(position+1));
        viewholder.getTv_title().setText(music.getTitle());
        viewholder.getTv_artist().setText(music.getArtist()+"-"+music.getAlbum());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }
}
