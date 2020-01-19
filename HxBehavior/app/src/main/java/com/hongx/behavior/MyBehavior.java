package com.hongx.behavior;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;


/**
 * @author: fuchenming
 * @create: 2020-01-19 09:35
 */
public class MyBehavior {

    public MyBehavior(Context context) {

    }

    public boolean layoutDependsOn(@NonNull View parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof NestedScrollView && dependency.getId() == R.id.scollView;
    }

    /**
     * 嵌套滑动中的方法
     *
     * @param parent
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     */
    public void onNestedScroll(@NonNull View parent, @NonNull View child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        //向下滑动了 滑动距离是负数 就是向下
        if (dyConsumed < 0) {
            //当前观察者控件的Y坐标小于等于0   并且 被观察者的Y坐标不能超过观察者控件的高度
            if (child.getY() <= 0 && target.getY() <= child.getHeight()) {
                child.setTranslationY(-(target.getScrollY() > child.getHeight() ?
                        child.getHeight() : target.getScrollY()));
                target.setTranslationY(-(target.getScrollY() > child.getHeight() ?
                        child.getHeight() : target.getScrollY()));
                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight() - child.getHeight() - child.getTranslationY());
                target.setLayoutParams(layoutParams);
            }
        } else {
            //向上滑动了 被观察者的Y坐标不能小于或者等于0
            if (target.getY() > 0) {
                //设置观察者的Y坐标的偏移  1.不能超过观察者自己的高度
                child.setTranslationY(-(target.getScrollY() > child.getHeight() ?
                        child.getHeight() : target.getScrollY()));
                target.setTranslationY(-(target.getScrollY() > child.getHeight() ?
                        child.getHeight() : target.getScrollY()));
                //获取到被观察者的LayoutParams
                ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
                //当我们向上滑动的时候  被观察者的高度 就等于 它父亲的高度 减去观察者的高度 再减去观察者Y轴的偏移值
                layoutParams.height = (int) (parent.getHeight() - child.getHeight() - child.getTranslationY());
                target.setLayoutParams(layoutParams);
            }
        }

    }


}
