package com.hongx.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;

import java.lang.reflect.Constructor;

/**
 * @author: fuchenming
 * @create: 2020-01-19 08:50
 */
public class MyNestedLinearLayout extends LinearLayout implements NestedScrollingParent2 {

    public MyNestedLinearLayout(Context context) {
        super(context);
    }

    public MyNestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 这个是嵌套滑动控制事件分发的控制方法，只有返回true才能接收到事件分发
     *
     * @param view  包含target的ViewParent的直接子View  嵌套滑動的子控件
     * @param view1 嵌套滑動的子控件
     * @param i     滑动的方向，数值和水平方向  這裡說的不是手勢  而是當前控件的需求  或者環境
     * @param i1    发起嵌套事件的类型 分为触摸（ViewParent.TYPE_TOUCH）和非触摸（ViewParent.TYPE_NON_TOUCH）
     * @return
     */
    @Override
    public boolean onStartNestedScroll(@NonNull View view, @NonNull View view1, int i, int i1) {
        Log.i("hongx", "11111111111111--->调用了onStartNestedScroll方法");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View view, @NonNull View view1, int i, int i1) {
        Log.i("hongx", "22222222222222--->调用了onNestedScrollAccepted方法");
    }

    @Override
    public void onStopNestedScroll(@NonNull View view, int i) {
        Log.i("hongx", "33333333333333--->调用了onStopNestedScroll方法");
    }

    /**
     * 在子View滑动过程中会通知这个嵌套滑动的方法，要想这里收到嵌套滑动事件必须在onStartNestedScroll返回true
     *
     * @param target 當前滑動的控件
     * @param dxConsumed    滑動的控件在水平方向已经消耗的距离
     * @param dyConsumed    滑動的控件在垂直方法已经消耗的距离
     * @param dxUnconsumed   滑動的控件在水平方向剩下的未消耗的距离
     * @param dyUnconsumed   滑動的控件在垂直方法剩下的未消耗的距离
     * @param type   发起嵌套事件的类型 分为触摸（ViewParent.TYPE_TOUCH）和非触摸（ViewParent.TYPE_NON_TOUCH）
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,int dxUnconsumed, int dyUnconsumed, int type) {
        int childCount = this.getChildCount();
        //遍历直接子控件
        for (int x = 0; x < childCount; x++) {
            View childAt = this.getChildAt(x);
            //当前属性对象是没有自定义属性的!!!!!
            MyLayoutParams lp = (MyLayoutParams) childAt.getLayoutParams();
            //获取到控件的myBehavior对象
            MyBehavior myBehavior = lp.behavior;
            //如果子控件设置了myBehavior
            if (myBehavior != null) {
                //判断当前的滑动的控件是不是当前子控件的被观察者
                if (myBehavior.layoutDependsOn(this, childAt, target)) {
                    myBehavior.onNestedScroll(this, childAt, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                }
            }
        }
    }


    @Override
    public void onNestedPreScroll(@NonNull View view, int i, int i1, @NonNull int[] ints, int i2) {
        Log.i("hongx", "555555555555555--->调用了onNestedPreScroll方法");
    }

    /**
     * 这个方法的作用其实就是定义当前你这个控件下所有的子控件使用的LayoutParams类
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(getContext(), attrs);
    }

    class MyLayoutParams extends LayoutParams {
        private MyBehavior behavior;

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //将自定义的属性交给一个TypedArray来管理
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.MyNestedLinearLayout);
            //通过TypedArray获取到我们定义的属性的值 Behavoir类名
            String className = a.getString(R.styleable.MyNestedLinearLayout_layout_behavior);
            //根据类名 将Behavoir实例化
            behavior = parseBehavior(c, attrs, className);
            //清空  不清空占内存
            a.recycle();
        }

        /**
         * 将Behavoir实例化
         */
        private MyBehavior parseBehavior(Context c, AttributeSet attrs, String className) {
            MyBehavior behavior = null;
            if (TextUtils.isEmpty(className)) {
                return null;
            }
            try {
                Class aClass = Class.forName(className);
                if (!MyBehavior.class.isAssignableFrom(aClass)) {
                    return null;
                }
                //去获取到它的构造方法
                Constructor<? extends MyBehavior> constructor = aClass.getConstructor(Context.class);
                constructor.setAccessible(true);
                behavior = constructor.newInstance(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return behavior;
        }


        public MyLayoutParams(int width, int height) {
            super(width, height);
        }

        public MyLayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public MyLayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public MyLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public MyLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
