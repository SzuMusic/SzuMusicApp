package com.szumusic.szumusicapp.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.utils.NoDoubleClickListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private static int repeat_time=5;
    private EditText phone;
    private Button send_confirm;
    private EditText confirm_num;
    private Button reg_next;

    private Handler handler =new Handler();
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // setSystemBarTransparent();
        reg_next= (Button) findViewById(R.id.reg_next);
        send_confirm=(Button)findViewById(R.id.send_confirmnum);
        phone=(EditText)findViewById(R.id.phone);
        confirm_num=(EditText)findViewById(R.id.confirm);

        send_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String phone_num=phone.getText().toString();
                if(phone_num.length()!=11){
                    Toast toast=Toast.makeText(RegisterActivity.this, "请输入正确的手机号码~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else {
                    send_confirm.setEnabled(false);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            send_confirm.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                    });

                    sendPhoneNum();
                    new Thread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void run() {
                            for (int i = 1; i <= repeat_time; i++) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                final int finalI = i;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        send_confirm.setText((repeat_time- finalI) + " s后重发");
                                    }
                                });
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    send_confirm.setEnabled(true);
                                    send_confirm.setText("发送验证码");
                                    send_confirm.setBackground(getResources().getDrawable(R.drawable.login_button));
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        reg_next.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String phone_num = phone.getText().toString();
                String temp = confirm_num.getText().toString();
                if (phone_num.length() != 11) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "请输入正确的手机号码~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (temp.length() == 0) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "请输入验证码~~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    int statu = sendConfirmNum(phone_num, temp);
                    System.out.println("statu====" + statu);
                    if (statu==1) {
                        Intent intent2 = new Intent(RegisterActivity.this, Register2Activity.class);
                        intent2.putExtra("phone_num", phone_num);
                        startActivity(intent2);
                    } else if(statu==0) {
                        Toast toast = Toast.makeText(RegisterActivity.this, "验证码错误~~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if(statu==2) {
                        Toast toast = Toast.makeText(RegisterActivity.this, "请检查你的网络是否连接~~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });
    }


    private void sendPhoneNum() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                String url="http://120.27.106.28/MusicGrade/getPhone";
                HttpURLConnection connection=null;
                try {
                    URL httpUrl=new URL(url);
                    connection= (HttpURLConnection) httpUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setReadTimeout(10000);
                    JSONObject jsonObject=new JSONObject();
                    String str=phone.getText().toString();
                    System.out.println("str========" + str);
                    jsonObject.put("phone", str);
                    String param="data="+jsonObject.toString();
                    System.out.println("===点击接收验证码后，传入的数据==" + param);
                    connection.connect();
                    DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(param);
                    outputStream.flush();
                    outputStream.close();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb=new StringBuffer();
                    String s;
                    while ((s=reader.readLine())!=null){
                        sb.append(s);
                    }
                    System.out.println("===点击接收验证码后，接收的数据==" + sb.toString());
                    JSONObject jsonObject1=new JSONObject(sb.toString());
                    String str2=jsonObject1.getString("flag");
                    // 取得sessionid.
                    String cookieval = connection.getHeaderField("set-cookie");
                    if(cookieval != null) {
                        sessionId = cookieval.substring(0, cookieval.indexOf(";"));
                    }
                    System.out.println("sessionid===" + sessionId);
                    if(str2.equals("1")){
                        Looper.prepare();
                        Toast toast=Toast.makeText(getApplicationContext(), "验证码已发送~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    else if(str2.equals("0")){
                        Toast toast=Toast.makeText(RegisterActivity.this, "验证码发送失败，请点击重发~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    else if(str2.equals("2")){
                        Looper.prepare();
                        Toast toast=Toast.makeText(RegisterActivity.this, "该手机号已被注册，请直接登录~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }  catch (Exception e) {
                    System.out.println("===点击接收验证码抛出错误===");
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }

    private int sendConfirmNum(final String phone_num,final String confirm_num) {
        final int[] statu = new int[1];
        final boolean[] isEnded = {false};
        new Thread(new Runnable(){
            @Override
            public void run() {
                String url="http://120.27.106.28/MusicGrade/sendConfirmNum";
                HttpURLConnection connection=null;
                try {
                    URL httpUrl=new URL(url);
                    connection= (HttpURLConnection) httpUrl.openConnection();
                    if(sessionId != null) {
                        connection.setRequestProperty("cookie", sessionId);
                    }
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setReadTimeout(10000);
                    JSONObject jsonObject=new JSONObject();
                    String str=phone.getText().toString();
                    System.out.println("str========" + str);
                    jsonObject.put("phone_num", phone_num);
                    jsonObject.put("confirm_num", confirm_num);
                    String param="data="+jsonObject.toString();
                    System.out.println("===点击继续游戏后，传入的数据==" + param);
                    connection.connect();
                    DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(param);
                    outputStream.flush();
                    outputStream.close();
                    System.out.println("===点击继续游戏后，传入的数据==" + param);
                    BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb=new StringBuffer();
                    String s;
                    while ((s=reader.readLine())!=null){
                        sb.append(s);
                    }
                    System.out.println("===点击继续游戏后，接受的数据=="+sb);
                    JSONObject jsonObject1=new JSONObject(sb.toString());
                    String str2= (String) jsonObject1.get("flag");
                    System.out.println("验证是否输入正确===" + str2 + " " + str2.equals("1"));
                    if(str2.equals("1"))
                        statu[0] =1;
                    else
                        statu[0] =0;
                    isEnded[0] =true;
                }  catch (Exception e) {
                    System.out.println("===点击继续游戏抛出错误===");
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
        int times=10;
        while (!isEnded[0]){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(times--<=0){
                statu[0]=2;
                break;
            }
        }
        return statu[0];
    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#EE2755"));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}

