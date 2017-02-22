package com.szumusic.szumusicapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.szumusic.szumusicapp.R;
import com.szumusic.szumusicapp.utils.NoDoubleClickListener;

public class Login2Activity extends AppCompatActivity {
    private TextView reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        reg= (TextView) findViewById(R.id.reg);
        reg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                reg();
            }
        });
    }

    private void reg() {
        Intent intent2=new Intent(Login2Activity.this,RegisterActivity.class);
        startActivity(intent2);
        Login2Activity.this.finish();
    }

}
