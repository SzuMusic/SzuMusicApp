package com.szumusic.szumusicapp.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.ui.music.player.MyLocationListener;
import com.szumusic.szumusicapp.ui.music.player.PlayService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
 /*   public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();*/
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* System.out.println(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocation();
        mLocationClient.start();*/

        setSystemBarTransparent();
        Intent intent=new Intent(MainActivity.this, PlayService.class);
        startService(intent);
        sp=this.getSharedPreferences("userinfo",MODE_PRIVATE);
        if(sp.getBoolean("isChecked",false)){
                    Intent intent2=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent2);
                    finish();
        }
        else {
                    Intent intent2 = new Intent(MainActivity.this, Login2Activity.class);
                    startActivity(intent2);
                    finish();
        }
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
