package com.szumusic.szumusicapp.ui.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class Login2Activity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login_in_button;
    private String user_id;
    private String e_name;

    private TextView reg;
    private LinearLayout login_layout;
    private float mScreenHeight;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setSystemBarTransparent();
        login_layout= (LinearLayout) findViewById(R.id.login_layout);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        imageRun();

        sp=this.getSharedPreferences("userinfo",MODE_PRIVATE);
        if(sp.getBoolean("isChecked",false)){
            Intent intent2=new Intent(Login2Activity.this,HomeActivity.class);
            startActivity(intent2);
            finish();
        }

        username= (EditText) findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login_in_button=(Button)findViewById(R.id.login_in_button);
        login_in_button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                loginIn();
            }
        });


        reg= (TextView) findViewById(R.id.reg);
        reg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                reg();
            }
        });
    }

    private void loginIn() {
        String temp_username=username.getText().toString();
        String temp_password=password.getText().toString();
        if (temp_username.length()==0||temp_password.length()==0){
            Toast toast=Toast.makeText(Login2Activity.this, "手机号和密码不能为空~", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if (temp_username.length()!=11){
            Toast toast=Toast.makeText(Login2Activity.this, "请输入正确的手机号~", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else if(temp_password.length()<6||temp_password.length()>30){
            Toast toast=Toast.makeText(Login2Activity.this, "请输入正确的手机号或密码~", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else{
            int statu=sendUserAndPsw(temp_username,temp_password);
            if(statu==1){
                SharedPreferences.Editor editor = sp.edit(); //获取编辑器
                editor.putBoolean("isChecked", true);
                editor.putString("user_id", user_id);
                editor.putString("e_name",e_name);
                editor.commit();
                Intent intent2=new Intent(Login2Activity.this,HomeActivity.class);
                startActivity(intent2);
            }
            else if(statu==0){
                Toast toast=Toast.makeText(Login2Activity.this, "请输入正确的手机号或密码~~", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else if (statu==2){
                Toast toast=Toast.makeText(Login2Activity.this, "请检查你的网络状况~~", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    private int sendUserAndPsw(final String temp_username, final String temp_password) {
        final int[] statu = new int[1];
        final boolean[] isEnded = {false};
        new Thread(new Runnable(){
            @Override
            public void run() {
                String url="http://172.31.69.182:8080/MusicGrade/pLoginIn";
                HttpURLConnection connection=null;
                try {
                    URL httpUrl=new URL(url);
                    connection= (HttpURLConnection) httpUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setReadTimeout(10000);

                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("username", URLEncoder.encode(temp_username, "UTF-8"));
                    jsonObject.put("password", temp_password);
                    String param="data="+jsonObject.toString();
                    System.out.println("===点击登录后，传入的数据==" + param);
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
                    System.out.println("===点击登录后，接受的数据=="+sb);
                    JSONObject jsonObject1=new JSONObject(sb.toString());
                    String str2= (String) jsonObject1.get("flag");
                    if(str2.equals("1")) {
                        e_name=jsonObject1.getString("e_name");
                        user_id=jsonObject1.getString("user_id");
                        statu[0] = 1;
                    }
                    else if(str2.equals("0"))
                        statu[0] =0;

                    in.close();
                    reader.close();
                    isEnded[0]=true;
                }  catch (Exception e) {
                    System.out.println("===点击登录抛出错误===");
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


    private void imageRun() {
        System.out.println("===="+mScreenHeight+" "+login_layout.getHeight());
        ValueAnimator animator = ValueAnimator.ofFloat(mScreenHeight
                - login_layout.getHeight(),0);
        animator.setTarget(login_layout);
        animator.setDuration(1500).start();
        // animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                login_layout.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }


    private void reg() {
        Intent intent2=new Intent(Login2Activity.this,RegisterActivity.class);
        startActivity(intent2);
        //Login2Activity.this.finish();
    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
