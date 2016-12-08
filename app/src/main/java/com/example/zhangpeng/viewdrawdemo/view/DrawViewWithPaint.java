package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 使用 paint 画笔画图练习
 * Created by zhangpeng on 2016/12/7.
 */
public class DrawViewWithPaint extends View {
    private Paint mPaint100;
    private Rect rect100;
    private Paint mPaint200;
    private Rect rect200;

    public DrawViewWithPaint(Context context) {
        super(context);
        init();
    }

    public DrawViewWithPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawViewWithPaint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint100 = new Paint();
        mPaint100.setColor(Color.BLUE);
        rect100 = new Rect(100, 100, 200, 200);

        mPaint200=new Paint();
        mPaint200.setColor(Color.RED);
        rect200=new Rect(150,150,150,150);
    }



    /**
     * 这里进行复写 onMeasure 方法，让自定义的 View 支持 Wrap_content 模式
     * 这里指定当使用该自定义的 View 时候，使用了 Wrap_content 属性后
     * 该 View  的宽和高都为 200dp ，这个尺寸需要根据具体需要和情况进行计算
     * 这里只是为了解释这个原理，任意给定了一个值而已
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // wrap_content 模式下给定宽/高质
        int width=200;
        int height=200;
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int withSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode==MeasureSpec.AT_MOST&&heightMode==MeasureSpec.AT_MOST){
            //宽和高都为 wap_content 模式，进行设定值
            setMeasuredDimension(width,height);
        }else if(widthMode==MeasureSpec.AT_MOST){
            //如果只有宽为 wrap_content 模式
            setMeasuredDimension(width,heightSize);
        }else if(heightMode==MeasureSpec.AT_MOST){
            //如果只有高为 wrap_content 模式
            setMeasuredDimension(withSize,height);
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect100,mPaint100);
        canvas.translate(-50,-50);
        canvas.drawRect(rect200,mPaint200);
        canvas.drawText("你好打完少",100,100,mPaint200);
    }
}
