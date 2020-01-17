package com.hongx.splashview;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by barry on 2018/7/10.
 */

public class SplashView extends View {
    private ValueAnimator mAnimator;
    // 大圆(里面包含很多小圆的)的半径
    private float mRotationRadius = 90;
    // 每一个小圆的半径
    private float mCircleRadius = 18;
    // 小圆圈的颜色列表，在initialize方法里面初始化
    private int[] mCircleColors;
    // 大圆和小圆旋转的时间
    private long mRotationDuration = 1200; //ms
    // 第二部分动画的执行总时间(包括二个动画时间，各占1/2)
    private long mSplashDuration = 1200; //ms
    // 整体的背景颜色
    private int mSplashBgColor = Color.WHITE;

    /**
     * 参数，保存了一些绘制状态，会被动态地改变*
     */
    //空心圆初始半径
    private float mHoleRadius = 0F;
    //当前大圆旋转角度(弧度)
    private float mCurrentRotationAngle = 0F;
    //当前大圆的半径
    private float mCurrentRotationRadius = mRotationRadius;

    // 绘制圆的画笔
    private Paint mPaint = new Paint();
    // 绘制背景的画笔
    private Paint mPaintBackground = new Paint();

    // 屏幕正中心点坐标
    private float mCenterX;
    private float mCenterY;
    //屏幕对角线一半
    private float mDiagonalDist;
    private SplashState mState = null;

    public SplashView(Context context) {
        super(context);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        mDiagonalDist = (float) Math.sqrt((w * w + h * h)) / 2f;//勾股定律
    }

    private void init(Context context) {
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);
        //画笔初始化
        //消除锯齿
        mPaint.setAntiAlias(true);
        mPaintBackground.setAntiAlias(true);
        //设置样式---边框样式--描边
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setColor(mSplashBgColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null) {
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }


    private abstract class SplashState {
        public abstract void drawState(Canvas canvas);

        public void cencel() {
            mAnimator.cancel();
        }
    }

    //1、旋转动画
    private class RotateState extends SplashState {
        public RotateState() {
            //1、动画的初始化工作，2、开启动画
            mAnimator = ValueAnimator.ofFloat(0f, (float) Math.PI * 2);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentRotationAngle = (float) valueAnimator.getAnimatedValue();
                    postInvalidate();
                }
            });
            mAnimator.setDuration(mRotationDuration);
            //无限循环
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            //背景
            drawBackground(canvas);
            //绘制小圆
            drawCircles(canvas);
        }
    }

    //执行聚合动画
    public void splashDisappear() {
        if (mState != null && mState instanceof RotateState) {
            RotateState rotateState = (RotateState) mState;
            rotateState.cencel();
            post(new Runnable() {
                @Override
                public void run() {
                    mState = new MergingState();
                }
            });
        }
    }

    //2、聚合动画
    private class MergingState extends SplashState {
        public MergingState() {
            mAnimator = ValueAnimator.ofFloat(0f, mRotationRadius);
            mAnimator.setDuration(mRotationDuration);
            mAnimator.setInterpolator(new OvershootInterpolator(10f));
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentRotationRadius = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            //反转
            mAnimator.reverse();
        }

        @Override
        public void drawState(Canvas canvas) {
            //背景
            drawBackground(canvas);
            //绘制小圆
            drawCircles(canvas);
        }
    }

    //3、水波纹扩散动画
    private class ExpandState extends SplashState {
        public ExpandState() {
            mAnimator = ValueAnimator.ofFloat(mCircleRadius, mDiagonalDist);
            mAnimator.setDuration(mRotationDuration);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mHoleRadius = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.start();

        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }


    private void drawBackground(Canvas canvas) {
        if (mHoleRadius > 0f) {
            mPaintBackground.setColor(Color.BLACK);
            float strokeWidth = mDiagonalDist - mHoleRadius;
            mPaintBackground.setStrokeWidth(strokeWidth);
            float radius = mHoleRadius + strokeWidth / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mPaintBackground);
        } else {
            canvas.drawColor(mSplashBgColor);
        }
    }

    private void drawCircles(Canvas canvas) {
        //每个小圆之间的间隔角度= 2π /  小圆的个数
        float rotetionAngle = (float) (2 * Math.PI / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            /**
             * x= r* cos(a) + centerX
             * y = r* sin(a) + centerY
             */
            double angle = i * rotetionAngle + mCurrentRotationAngle;
            float cx = (float) (mCurrentRotationRadius * Math.cos(angle) + mCenterX);
            float cy = (float) (mCurrentRotationRadius * Math.sin(angle) + mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }
}

