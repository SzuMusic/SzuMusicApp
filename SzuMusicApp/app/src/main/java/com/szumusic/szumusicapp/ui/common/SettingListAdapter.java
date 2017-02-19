package com.szumusic.szumusicapp.ui.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;

/**
 * Created by kobe_xuan on 2017/2/18.
 */
public class SettingListAdapter extends RecyclerView.Adapter {
    private String[] titles = new String[]{"我的消息", "会员中心", "我的好友", "切换模式","定时关机","系统设置","关于我们"};



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_list_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          Viewholder viewholder= (Viewholder) holder;
          switch (position){
              case 0:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_message);
                  break;
              case 1:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_vip);
                  break;
              case 2:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_firends);
                  break;
              case 3:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_modle);
                  break;
              case 4:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_turnoff);
                  break;
              case 5:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_system);
                  break;
              case 6:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_about);
                  break;
          }
        ((Viewholder) holder).getTv_title().setText(titles[position]);
    }
    class Viewholder extends RecyclerView.ViewHolder {
        ImageView setting_icon;
        TextView tv_title;
        public Viewholder(View itemView) {
            super(itemView);
            setting_icon=(ImageView)itemView.findViewById(R.id.setting_icon);
            tv_title=(TextView)itemView.findViewById(R.id.tv_title);

        }
        public ImageView getSetting_icon() {
            return setting_icon;
        }
        public TextView getTv_title() {
            return tv_title;
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
