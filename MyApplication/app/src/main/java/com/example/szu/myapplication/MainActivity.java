package com.example.szu.myapplication;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hsalf.smilerating.SmileRating;

import jaygoo.widget.wlv.WaveLineView;
import online.osslab.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    Dialog markDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CircleProgressBar circleProgressBar = (CircleProgressBar )findViewById(R.id.myCircleProgressBar );
//       circleProgressBar.setProgress((float) 90);
        WaveLineView waveLineView= (WaveLineView) findViewById(R.id.waveLineView);
        waveLineView.startAnim();
        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                System.out.println("用户选择了");
                switch (smiley) {
                    case SmileRating.BAD:
                        System.out.println("用户选择了BAD");
                        break;
                    case SmileRating.GOOD:
                        System.out.println("用户选择了GOOD");
                        break;
                    case SmileRating.GREAT:
                        System.out.println("用户选择了great");
                        break;
                    case SmileRating.OKAY:
                        System.out.println("用户选择了ok");
                        break;
                    case SmileRating.TERRIBLE:
                        System.out.println("用户选择了trouble");
                        break;
                }
            }
        });
        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                System.out.println("用户选择了");
            }
        });
    }
}
