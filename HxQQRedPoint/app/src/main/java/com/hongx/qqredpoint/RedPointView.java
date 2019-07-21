package com.hongx.qqredpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by qijian on 16/5/23.
 */
public class RedPointView extends FrameLayout {
    private PointF mStartPoint, mCurPoint;
    private int mRadius = 20;
    private Paint mPaint;
    private Path mPath;
    private boolean mTouch = false;

    public RedPointView(Context context) {
        super(context);
        initView();
    }

    public RedPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RedPointView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {

        mStartPoint = new PointF(100, 100);
        mCurPoint = new PointF();

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
    }


    private void calculatePath() {

        float x = mCurPoint.x;
        float y = mCurPoint.y;
        float startX = mStartPoint.x;
        float startY = mStartPoint.y;
        float dx = x - startX;
        float dy = y - startY;
        double a = Math.atan(dy / dx);
        float offsetX = (float) (mRadius * Math.sin(a));
        float offsetY = (float) (mRadius * Math.cos(a));

        // 根据角度算出四边形的四个点
        float x1 = startX + offsetX;
        float y1 = startY - offsetY;

        float x2 = x + offsetX;
        float y2 = y - offsetY;

        float x3 = x - offsetX;
        float y3 = y + offsetY;

        float x4 = startX - offsetX;
        float y4 = startY + offsetY;

        float anchorX = (startX + x) / 2;
        float anchorY = (startY + y) / 2;

        mPath.reset();
        mPath.moveTo(x1, y1);
        mPath.quadTo(anchorX, anchorY, x2, y2);
        mPath.lineTo(x3, y3);
        mPath.quadTo(anchorX, anchorY, x4, y4);
        mPath.lineTo(x1, y1);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.saveLayer(new RectF(0, 0, getWidth(), getHeight()), mPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, mRadius, mPaint);
        if (mTouch) {
            calculatePath();
            canvas.drawCircle(mCurPoint.x, mCurPoint.y, mRadius, mPaint);
            canvas.drawPath(mPath, mPaint);
        }
        canvas.restore();

        super.dispatchDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mTouch = true;
            }
            break;
            case MotionEvent.ACTION_UP: {
                mTouch = false;
            }
        }
        mCurPoint.set(event.getX(), event.getY());
        postInvalidate();
        return true;
    }
}
