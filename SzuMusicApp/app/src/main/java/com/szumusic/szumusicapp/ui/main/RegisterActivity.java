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

public class RegisterActivity extends AppCompatActivity {
    private Button reg_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reg_next= (Button) findViewById(R.id.reg_next);
        reg_next.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                reg_next();
            }
        });
    }
    private void reg_next() {
        Intent intent2=new Intent(RegisterActivity.this,Register2Activity.class);
        startActivity(intent2);
        RegisterActivity.this.finish();
    }
}

