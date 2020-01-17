package com.hongx.splashview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private SplashView splashView;
    private FrameLayout mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainView = new FrameLayout(this);
        ContentView contentView = new ContentView(this);
        mMainView.addView(contentView);
        splashView = new SplashView(this);
        mMainView.addView(splashView);
        setContentView(mMainView);
        startLoaddData();
    }

    private void startLoaddData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //表示数据加载完毕，进入第二个状态
                splashView.splashDisappear();
            }
        }, 3000);//延时时间
    }
}
