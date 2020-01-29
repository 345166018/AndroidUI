package com.hongx.douyin;

import android.view.View;

public interface OnViewPagerListener {
    //停止播放的监听方法
    void onPageRelease(View itemView);

    //播放的监听方法
    void onPageSelected(View itemView);
}
