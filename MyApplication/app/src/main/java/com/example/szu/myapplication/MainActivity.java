package com.example.szu.myapplication;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jaygoo.widget.wlv.WaveLineView;
import online.osslab.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CircleProgressBar circleProgressBar = (CircleProgressBar )findViewById(R.id.myCircleProgressBar );
//       circleProgressBar.setProgress((float) 90);
        WaveLineView waveLineView= (WaveLineView) findViewById(R.id.waveLineView);
        waveLineView.startAnim();
    }
}
