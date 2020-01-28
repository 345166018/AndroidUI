package com.hongx.chinamap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * @author: fuchenming
 * @create: 2020-01-26 20:10
 */
public class ProviceItem {

    //path对象
    private Path path;
    //绘制的颜色
    private int drawColor;

    public ProviceItem(Path path) {
        this.path = path;
    }


    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    void drawItem(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            //选中时，绘制描边效果
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawPath(path, paint);
        } else {
            //这是不选中的情况下   设置边界
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8, 0, 0, 0xffffff);
            canvas.drawPath(path, paint);
            //后面是填充
            paint.clearShadowLayer();
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(path, paint);
        }
    }

    public boolean isTouch(float x, float y) {
        //创建一个矩形
        RectF rectF = new RectF();
        //获取到当前省份的矩形边界
        path.computeBounds(rectF, true);
        //创建一个区域对象
        Region region = new Region();
        //将path对象放入到Region区域对象中
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        //返回是否这个区域包含传进来的坐标
        return region.contains((int) x, (int) y);
    }

}
