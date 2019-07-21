package com.hongx.qqredpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author: yaichain18
 * @create: 2019-07-15 08:27
 */
public class WaterView extends FrameLayout {

    //定义一个文本控件
    TextView textView;

    //文本框的初始坐标
    private PointF initPosition;
    //手指移动到的坐标
    private PointF movePosition;
    private boolean isClicked = false;
    //绘制的圆的半径
    private float mRadius = 40;
    //绘制的画笔
    private Paint mPaint;
    //存储连接桥的对象
    private Path mPath;

    //判断文本框是否离开某个范围
    private boolean isOut = false;

    //爆炸效果的图片控件
    private ImageView imageView;

    public WaterView(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化整个效果的控件
     */
    private void init() {

        initPosition = new PointF(500, 500);
        movePosition = new PointF();

        //初始化画笔的样式为填充
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();

        textView = new TextView(getContext());
        textView.setPadding(20, 20, 20, 20);
        textView.setTextColor(Color.WHITE);
        textView.setText("99+");
        textView.setBackgroundResource(R.drawable.tv_bg);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);

        this.addView(textView);

        imageView = new ImageView(getContext());
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.tip_anim);
        this.addView(imageView);

    }

    /**
     * 绘制当前控件里面的内容的控件
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        //保存canvas的状态
        canvas.save();

        if (isClicked) {
            textView.setX(movePosition.x - textView.getWidth() / 2);
            textView.setY(movePosition.y - textView.getHeight() / 2);

            drawPath();

            if(!isOut){
                //画两个圆
                //画第一个圆，是初始化坐标的圆
                canvas.drawCircle(initPosition.x,initPosition.y,mRadius,mPaint);
                //画第二个圆，是终点的圆
                canvas.drawCircle(movePosition.x,movePosition.y,mRadius,mPaint);
                //画连接桥
                canvas.drawPath(mPath,mPaint);
            }

        } else {
            //设置初始坐标为控件的中心点
            textView.setX(initPosition.x - textView.getWidth() / 2);
            textView.setY(initPosition.y - textView.getHeight() / 2);
        }

        // 恢复canvas的状态
        canvas.restore();
        super.dispatchDraw(canvas);

    }

    public void drawPath(){
        //获取到终点与起点的X坐标的差值 A2
        float widthX = movePosition.x - initPosition.x;
        //获取到终点与起点的Y坐标的差值 A3
        float widthY = movePosition.y - initPosition.y;

        //获取两个点之间的直线距离
        float s = (float) Math.sqrt(Math.pow(widthX,2) + Math.pow(widthY,2));

        mRadius = 40 - s/30;

        if(s >= 400){
            isOut = true;
        }else {
            isOut = false;
        }

        //得到三角形的锐角的角度值 正切值
        double atan = Math.atan(widthY / widthX);

        //获取到offsetX的长度
        float offsetX = (float) (mRadius * Math.sin(atan));
        //获取到offsetY的长度
        float offsetY = (float) (mRadius * Math.cos(atan));


        //获取到A坐标
        float AX = initPosition.x + offsetX;
        float AY = initPosition.y - offsetY;
        //获取到B坐标
        float BX = movePosition.x + offsetX;
        float BY = movePosition.y - offsetY;
        //获取到C坐标
        float CX = movePosition.x - offsetX;
        float CY = movePosition.y + offsetY;
        //获取到D坐标
        float DX = initPosition.x - offsetX;
        float DY = initPosition.y + offsetY;

        //获取到起点坐标跟终点坐标的中心点
        float conX = (initPosition.x + movePosition.x)/2;
        float conY = (initPosition.y + movePosition.y)/2;

        //初始化path对象
        mPath.reset();
        //将起点移动到A坐标
        mPath.moveTo(AX,AY);
        //从A坐标连接到B坐标
        mPath.quadTo(conX,conY,BX,BY);
        //从B点连接到C点
        mPath.lineTo(CX,CY);
        //从C点连接到D点
        mPath.quadTo(conX,conY,DX,DY);
        //从D点连接到A点
        mPath.lineTo(AX,AY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                movePosition.set(initPosition.x, initPosition.y);

                //判断当前位置是否在文本控件里面
                //这个对象是用来封装文本控件的范围的对象
                Rect rect = new Rect();
                int[] location = new int[2];

                //获取到textView控件在窗体中的X，Y坐标
                textView.getLocationOnScreen(location);

                //初始化Rect对象
                rect.left = location[0];
                rect.top = location[1];
                rect.right = location[0] + textView.getWidth();
                rect.bottom = location[1] + textView.getHeight();

                //判断当前点击的坐标是否是在范围之内
                //getRawX和getRawY是相对于父控件
                if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    isClicked = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                isClicked = false;
//                movePosition.set(initPosition.x, initPosition.y);

                if(isOut){
                    textView.setVisibility(View.GONE);
                    imageView.setX(movePosition.x - imageView.getWidth()/2);
                    imageView.setY(movePosition.y - imageView.getHeight()/2);
                    imageView.setVisibility(View.VISIBLE);
                    ((AnimationDrawable) imageView.getDrawable()).start();

                }
                break;
            case MotionEvent.ACTION_MOVE:
                //getX和getY是相对于屏幕的坐标
                movePosition.set(event.getX(), event.getY());
                break;

        }

        //通过这个API可以调用到dispatchDraw的方法
        postInvalidate();
        return true;
    }
}
