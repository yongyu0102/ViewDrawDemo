package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Canvas 画布使用练习
 * Created by zhangpeng on 2016/12/11.
 */

public class CanvasUseModelView extends View {
    private Paint mPaint00;
    private Paint mPaint100;
    private Paint mPaint80;
    private Paint mPaint200;
    private Paint mPaint;

    private Rect rect100;
    private Rect rect200;
    private Rect rect;

    private Bitmap bitmap;
    private Context context;

    public CanvasUseModelView(Context context) {
        super(context);
        init(context);
    }

    public CanvasUseModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CanvasUseModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mPaint100 = new Paint();
        mPaint100.setColor(Color.BLUE);
        //这里的 100,200为相当于当前绘制 View 的坐标值，值单位为像素，坐标原点
        //如果当前画布没有进行移动坐标原点为当前 View 的左上角

        rect100 = new Rect(100, 100, 200, 200);

        mPaint200 = new Paint();
        mPaint200.setColor(Color.RED);
        rect200 = new Rect(200, 200, 400, 400);

        mPaint00 = new Paint();
        mPaint00.setColor(Color.GREEN);

        mPaint80 = new Paint();
        mPaint80.setColor(Color.YELLOW);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        rect = new Rect();

        recording();    // 调用录制
        //从已有图片获取 bitmap
        getBitMap();
    }

    private void getBitMap() {
        try {
            InputStream inputStream = context.getAssets().open("canvas02.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


    /**
     * 这里进行复写 onMeasure 方法，让自定义的 View 支持 Wrap_content 模式
     * 这里指定当使用该自定义的 View 时候，使用了 Wrap_content 属性后
     * 该 View  的宽和高都为 200dp ，这个尺寸需要根据具体需要和情况进行计算
     * 这里只是为了解释这个原理，任意给定了一个值而已
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // wrap_content 模式下给定宽/高质
        int width = 200;
        int height = 200;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            //宽和高都为 wap_content 模式，进行设定值
            setMeasuredDimension(width, height);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //如果只有宽为 wrap_content 模式
            setMeasuredDimension(width, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //如果只有高为 wrap_content 模式
            setMeasuredDimension(withSize, height);
        }
    }

    /**
     * 使用 Picture 进行对 canvas 录制
     */
    // 1.创建Picture
    private Picture mPicture = new Picture();

    // 2.录制内容方法
    private void recording() {
        // 开始录制 (接收返回值Canvas)
        Canvas canvas = mPicture.beginRecording(500, 500);
        // 创建一个画笔
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

        // 在Canvas中具体操作
        // 位移
        canvas.translate(250, 250);
        // 绘制一个圆
        canvas.drawCircle(0, 0, 80, paint);

        mPicture.endRecording();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // drawPictrue 使用
//        canvas.drawPicture(mPicture,new RectF(0,0,mPicture.getWidth(),mPicture.getHeight()));
        //将 bitmap 绘制到 canvas 上进行显示，从左上角开始
//        canvas.drawBitmap(bitmap,new Matrix(),new Paint());

        //指定距离左上角坐标原点距离
//        canvas.drawBitmap(bitmap,200,500,new Paint());
        drawBitmapPart(canvas);
    }

    private void drawBitmapPart(Canvas canvas) {
        // 将画布坐标系移动到画布中央
        canvas.translate(getWidth() / 2, getHeight() / 2);

// 指定图片绘制区域(左上角的四分之一)
        Rect src = new Rect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

// 指定图片在屏幕上显示的区域
        Rect dst = new Rect(0, 0, 200, 400);

// 绘制图片
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    private void rotateTest03(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(0, 0, getWidth() / 2 - 80, mPaint);          // 绘制两个圆形
        canvas.drawCircle(0, 0, getWidth() / 2 - 110, mPaint);

        for (int i = 0; i <= 360; i += 10) {               // 绘制圆形之间的连接线
            //从竖直方向开始逐渐偏移10度逐一划线
            canvas.drawLine(0, getWidth() / 2 - 80, 0, getWidth() / 2 - 110, mPaint);
            canvas.rotate(10);
        }
    }

    private void rotateTest02(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        rect.set(0, -400, 400, 0);   // 矩形区域

        mPaint.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect, mPaint);

        canvas.rotate(180, 200, 0);               // 旋转180度 <-- 旋转中心向右偏移200个单位

        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect, mPaint);
    }

    private void rotateTest01(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        rect.set(0, -400, 400, 0);   // 矩形区域

        mPaint.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect, mPaint);

        canvas.rotate(180);                     // 旋转180度 <-- 默认旋转中心为原点

        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect, mPaint);
    }

    private void scaleTest05(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        rect.set(-400, -400, 400, 400);   // 矩形区域
        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i <= 20; i++) {
            canvas.scale(0.8f, 0.8f);
            canvas.drawRect(rect, mPaint);
        }
    }

    private void scaleTest04(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        rect.set(0, -400, 400, 0);   // 矩形区域

        mPaint.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect, mPaint);

        // 画布缩放  <-- 缩放中心向右偏移了200个单位
        //即坐标轴向右偏移了 200 个单位
        canvas.scale(-0.5f, -0.5f, 200, 0);

        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect, mPaint);
    }

    private void scaleTest03(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        rect.set(0, -400, 400, 0);   // 矩形区域

        mPaint.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect, mPaint);

        canvas.scale(-0.5f, -0.5f);          // 画布缩放

        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect, mPaint);
    }

    /**
     * 指定特定坐标点进行缩放
     *
     * @param canvas
     */
    private void scaleTest02(Canvas canvas) {
        // 将坐标系原点移动到画布正中心
        canvas.translate(getWidth() / 2, getHeight() / 2);

        rect100.set(0, -400, 400, 0);   // 矩形区域

        mPaint00.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect100, mPaint00);

        //以坐标点（200,0）为坐标点进行缩放
        canvas.scale(0.5f, 0.5f, 200, 0);          // 画布缩放  <-- 缩放中心向右偏移了200个单位

        mPaint00.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect100, mPaint00);
    }

    /**
     * 以默认坐标点进行缩放
     *
     * @param canvas
     */
    private void scaleTest01(Canvas canvas) {
        //将画布坐标原点移动到整个 view 的中心位置
        canvas.translate(getWidth() / 2, getHeight() / 2);
        rect100.set(0, -400, 400, 0);
        mPaint00.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect100, mPaint00);

        canvas.scale(0.5f, 0.5f);                // 画布缩放

        mPaint00.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect100, mPaint00);
    }

    /**
     * 平移示例
     *
     * @param canvas
     */
    private void translateTest(Canvas canvas) {
        canvas.drawCircle(0, 0, 100, mPaint00);

        mPaint00.setColor(Color.RED);
        canvas.translate(200, 200);
        canvas.drawCircle(0, 0, 100, mPaint00);

        mPaint00.setColor(Color.BLACK);
        canvas.translate(200, 200);
        canvas.drawCircle(0, 0, 100, mPaint00);
    }
}
