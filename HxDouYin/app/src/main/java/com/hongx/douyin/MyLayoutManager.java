package com.hongx.douyin;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MyLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {

    //根据这个参数来判断当前是上滑  还是下滑
    private int mDrift;
    //传进来的监听接口类
    private OnViewPagerListener onViewPagerListener;
    //解决吸顶或者洗低的对象
    private PagerSnapHelper pagerSnapHelper;


    public MyLayoutManager(Context context) {
        super(context);
    }

    public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        pagerSnapHelper = new PagerSnapHelper();
    }


    /**
     * 当MyLayoutManager完全放入到RecyclerView中的时候会被调用
     */
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        pagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    /**
     * 将Item添加进来的时候  调用这个方法
     */
    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (mDrift > 0) {
            //向上滑
            if (onViewPagerListener != null) {
                //如果是向上滑动的时候  就选中当前itemView下一个item
                onViewPagerListener.onPageSelected(view);
            }
        } else {
            //向下滑
            if (onViewPagerListener != null) {
                //如果是向上滑动的时候  就选中当前itemView下一个item
                onViewPagerListener.onPageSelected(view);
            }
        }
    }

    /**
     * 监听滑动的状态
     */
    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                //现在拿到的就是当前显示的这个item
                View snapView = pagerSnapHelper.findSnapView(this);
                assert snapView != null;
                if (onViewPagerListener != null) {
                    onViewPagerListener.onPageSelected(snapView);
                }
                break;
        }
        super.onScrollStateChanged(state);
    }

    /**
     * 将Item移除出去的时候  调用这个方法
     */
    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        Log.e("EEEEEEEEE", "22222222222222222");
        if (mDrift >= 0) {
            //向上滑
            if (onViewPagerListener != null) {
                onViewPagerListener.onPageRelease(view);
            }
        } else {
            //向下滑
            if (onViewPagerListener != null) {
                onViewPagerListener.onPageRelease(view);
            }
        }
    }


    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener) {
        this.onViewPagerListener = onViewPagerListener;
    }
}
