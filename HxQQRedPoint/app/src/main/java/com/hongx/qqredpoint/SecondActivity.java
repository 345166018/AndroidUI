package com.hongx.qqredpoint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * @author: yaichain18
 * @create: 2019-07-15 08:32
 */
public class SecondActivity extends AppCompatActivity {

    WaterView waterView;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    private void initView(){
        frameLayout = findViewById(R.id.frameLayout);
        layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        waterView = new WaterView(this);
        frameLayout.removeAllViews();
        frameLayout.addView(waterView);
    }

}
