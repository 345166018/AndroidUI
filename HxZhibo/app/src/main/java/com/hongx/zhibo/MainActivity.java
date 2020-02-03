package com.hongx.zhibo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hongx.zhibo.utils.MagicTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 加载直播fragment
        LiveFrag liveFrag = new LiveFrag();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_root, liveFrag).commit();
        // 加载
        new InteractiveFrag().show(getSupportFragmentManager(), "InteractiveFrag");

    }
}
