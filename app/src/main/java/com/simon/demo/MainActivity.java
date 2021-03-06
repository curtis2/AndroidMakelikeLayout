package com.simon.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    LikeLayoutView likeLayoutView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashHandleUtil.getmInstance().init(this,"mobileteacher_helper");
        likeLayoutView= (LikeLayoutView) findViewById(R.id.like_layout);
        likeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeLayoutView.addHeart();
            }
        });
    }
}
