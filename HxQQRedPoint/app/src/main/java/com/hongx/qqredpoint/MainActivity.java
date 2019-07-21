package com.hongx.qqredpoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FrameLayout containerView = (FrameLayout) findViewById(R.id.container);

        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPointView redPointView = new RedPointView(MainActivity.this);
                redPointView.setLayoutParams(layoutParams);
                containerView.removeAllViews();
                containerView.addView(redPointView);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPointViewCopy redPointView = new RedPointViewCopy(MainActivity.this);
                redPointView.setLayoutParams(layoutParams);
                containerView.removeAllViews();
                containerView.addView(redPointView);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedPointControlVIew redPointView = new RedPointControlVIew(MainActivity.this);
                redPointView.setLayoutParams(layoutParams);
                containerView.removeAllViews();
                containerView.addView(redPointView);
            }
        });
    }

    public void toNext(View view) {
        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
        startActivity(intent);

    }
}