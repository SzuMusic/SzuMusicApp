package com.szumusic.szumusicapp.ui.common;

import android.app.Dialog;
import android.content.Context;
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

/**
 * Created by kobe_xuan on 2017/1/28.
 */
public class SongListAdapter extends RecyclerView.Adapter {
    private int songcount=0;//歌曲的数量
    private Context context;
    Dialog markDialog=null;//评分的弹框

    public SongListAdapter() {
        super();
    }

    public SongListAdapter(Context context,int songcount) {
        super();
        this.context=context;
        this.songcount=songcount;
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView order_number;
        ImageView btn_mark;
        public Viewholder(View itemView) {
            super(itemView);
            order_number=(TextView)itemView.findViewById(R.id.order_number);
            btn_mark= (ImageView) itemView.findViewById(R.id.btn_mark);
            btn_mark.setOnClickListener(this);
            order_number.setOnClickListener(this);
        }

        public TextView getOrder_number(){return order_number;}


        @Override
        public void onClick(View view) {
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
        viewholder.getOrder_number().setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return songcount;
    }
}
