package com.szumusic.szumusicapp.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.load.Encoder;
import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.utils.NoDoubleClickListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Register2Activity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText confirm_psw;
    private Button sign_in_button;

    private String user_id;
    private String e_name;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        confirm_psw= (EditText) findViewById(R.id.confirm_password);
        sign_in_button= (Button) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String temp_username = username.getText().toString();
                String temp_password = password.getText().toString();
                String temp_con_psw = confirm_psw.getText().toString();
                if (temp_username.length() == 0) {
                    Toast toast = Toast.makeText(Register2Activity.this, "昵称不能为空~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (temp_username.length() < 2 || temp_username.length() > 20) {
                    Toast toast = Toast.makeText(Register2Activity.this, "昵称需要大于1位且小于21位~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (temp_password.length() == 0 || temp_con_psw.length() == 0) {
                    Toast toast = Toast.makeText(Register2Activity.this, "密码和确认密码不能为空~~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (!temp_password.equals(temp_con_psw)) {
                    Toast toast = Toast.makeText(Register2Activity.this, "请确保密码和确认密码相同~~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (temp_password.length() < 6 || temp_password.length() > 30) {
                    Toast toast = Toast.makeText(Register2Activity.this, "密码需要大于5位且小于31位~~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    String phone_num = getIntent().getStringExtra("phone_num");
                    int statu = regUser(phone_num, temp_username, temp_password);
                    if (statu == 1) {
                        sp=Register2Activity.this.getSharedPreferences("userinfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit(); //获取编辑器
                        editor.putBoolean("isChecked", true);
                        editor.putString("user_id", user_id);
                        editor.putString("e_name", e_name);
                        editor.commit();
                        Intent intent2 = new Intent(Register2Activity.this, HomeActivity.class);
                        startActivity(intent2);
                        finish();
                    }  else if (statu == 2) {
                        Toast toast = Toast.makeText(Register2Activity.this, "非常抱歉，我们的系统提出了问题~~", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });

    }

    private int regUser(final String phone_num,final String temp_username, final String temp_password) {
        final int[] statu = new int[1];
        final boolean[] isEnded = {false};
        new Thread(new Runnable(){
            @Override
            public void run() {
                String url="http://172.29.108.242:8080/MusicGrade/regUser";
                HttpURLConnection connection=null;
                try {
                    URL httpUrl=new URL(url);
                    connection= (HttpURLConnection) httpUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setReadTimeout(10000);

                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("phone_num", phone_num);
                    jsonObject.put("username", URLEncoder.encode(temp_username,"UTF-8"));
                    jsonObject.put("password", temp_password);
                    String param="data="+jsonObject.toString();
                    System.out.println("===点击开启音乐之旅后，传入的数据==" + param);
                    connection.connect();
                    DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(param);
                    outputStream.flush();
                    outputStream.close();
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb=new StringBuffer();
                    String s;
                    while ((s=reader.readLine())!=null){
                        sb.append(s);
                    }
                    System.out.println("===点击开启音乐之旅后，接受的数据=="+sb);
                    JSONObject jsonObject1=new JSONObject(sb.toString());
                    String str2= (String) jsonObject1.get("flag");
                    if(str2.equals("1")) {
                        e_name=jsonObject1.getString("e_name");
                        user_id=jsonObject1.getString("user_id");
                        statu[0] = 1;
                    }

                    isEnded[0]=true;
                }  catch (Exception e) {
                    System.out.println("===点击开启音乐之旅抛出错误===");
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

/*    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#EE2755"));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }*/
}
