package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义一个圆形图，支持 wrap_content 和 padding 属性
 * Created by zhangpeng on 2016/9/11.
 */
public class MyCircleView extends View {
    private Paint paint;
    private int color= Color.RED;
    public MyCircleView(Context context) {
        super(context);
        init();
    }

    public MyCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint=new Paint();
        paint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取 view 最终宽和高
        int width=getWidth();
        int height=getHeight();
        //获取 padding 值
        int paddingLeft=getPaddingLeft();
        int paddingRight=getPaddingRight();
        int paddingTop=getPaddingTop();
        int paddingBottom=getPaddingBottom();
        //计算去掉 padding 的宽和高
        int withFinal=width-paddingLeft-paddingRight;
        int heightFinal=height-paddingTop-paddingBottom;
        //计算半径
        int radius=Math.min(withFinal/2,heightFinal/2);
        //绘制视图内容
        canvas.drawCircle(paddingLeft+withFinal/2,paddingTop+heightFinal/2,radius,paint);

    }
}
