package com.szumusic.szumusicapp.ui.common;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.main.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kobe_xuan on 2017/2/18.
 */
public class SettingListAdapter extends RecyclerView.Adapter {
    private Context context;
    private String[] titles = new String[]{"我的消息", "会员中心", "我的好友", "切换模式","定时关机","系统设置","关于我们","退出登录"};
    AlertDialog quit_dialog;

    public SettingListAdapter(Context context) {
        this.context = context;
    }

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
              default:
                  ((Viewholder) holder).getSetting_icon().setImageResource(R.drawable.setting_system);
                  break;
          }
        ((Viewholder) holder).getTv_title().setText(titles[position]);
    }
    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView setting_icon;
        TextView tv_title;
        public Viewholder(View itemView) {
            super(itemView);
            setting_icon=(ImageView)itemView.findViewById(R.id.setting_icon);
            tv_title=(TextView)itemView.findViewById(R.id.tv_title);
            tv_title.setOnClickListener(this);

        }
        public ImageView getSetting_icon() {
            return setting_icon;
        }
        public TextView getTv_title() {
            return tv_title;
        }

        @Override
        public void onClick(View view) {
            TextView quit_tv;
            TextView cancel_tv;
            int position=getAdapterPosition();
            switch (view.getId()){
                case R.id.tv_title:
                    if(position==7) {
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialog_view = inflater.inflate(R.layout.dialog_quit, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(dialog_view);
                        quit_tv= (TextView) dialog_view.findViewById(R.id.quit);
                        cancel_tv= (TextView) dialog_view.findViewById(R.id.cancel_quit);
                        quit_tv.setOnClickListener(this);
                        cancel_tv.setOnClickListener(this);
                        quit_dialog=builder.create();
                        quit_dialog.show();
                    }
                    break;
                case R.id.quit:
                    String url="http://172.31.69.182:8080/MusicGrade/pLogOut";
                    OkHttpClient client = new OkHttpClient();
                    Map<String, Object> map = new HashMap<String, Object>();
                   /* e_name=e_name_tv.getText().toString();
                    map.put("user_id",user_id);
                    map.put("e_name",e_name_tv.getText().toString());
                    map.put("age",spinner_age.getSelectedIndex());
                    map.put("sex",spinner_sex.getSelectedIndex());
                    System.out.println(spinner_age.getSelectedIndex()+"==="+spinner_age.getText());*/
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
                                Boolean isSucceed=myjson.getBoolean("flag");
                                /*if(isSucceed) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(HomeActivity.this, "信息修改成功~", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        userinfo_dialog.hide();
                                    }
                                });*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case R.id.cancel_quit:
                    quit_dialog.hide();
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
