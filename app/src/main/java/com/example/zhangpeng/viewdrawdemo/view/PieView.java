package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.zhangpeng.viewdrawdemo.bean.PieData;

import java.util.ArrayList;

/**
 * 自定义一个饼状图
 * Created by zhangpeng on 2016/12/8.
 */
public class PieView extends View {

    // 颜色表 (注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<PieData> mData;
    // 宽高
    private int mWidth, mHeight;
    // 画笔
    private Paint mPaint = new Paint();

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //画笔填充
        mPaint.setStyle(Paint.Style.FILL);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    /**
     * 尺寸变化会调用
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int withSize = MeasureSpec.getSize(widthMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//处理 wrapContent 情况
        if (withMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            withSize = 200;
            heightSize = 200;
        } else if (withMode == MeasureSpec.AT_MOST) {
            withSize = 200;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = 200;
        }
        setMeasuredDimension(withSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //数据为空结束方法
        if (null == mData)
            return;
        // 当前起始角度
        float currentStartAngle = mStartAngle;
        // 将画布坐标原点移动到中心位置
        canvas.translate(mWidth / 2, mHeight / 2);
        // 确定饼状图半径
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
        // 饼状图绘制区域
        RectF rect = new RectF(-r, -r, r, r);

        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            mPaint.setColor(pie.getColor());
            //绘制扇形，通过绘制区域 rect 其实角度 currentStartAngle 结束角度 pie.getAngle()
            canvas.drawArc(rect, currentStartAngle, pie.getAngle(), true, mPaint);
            //改变角度
            currentStartAngle += pie.getAngle();
        }
    }

    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    // 设置数据
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();   // 刷新重新绘制
    }

    // 初始化数据
    private void initData(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
            return;

        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            sumValue += pie.getValue();       //计算数值和

            int j = i % mColors.length;       //设置颜色
            pie.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue;   // 百分比
            float angle = percentage * 360;                 // 对应的角度

            pie.setPercentage(percentage);                  // 记录百分比
            pie.setAngle(angle);                            // 记录角度大小
            sumAngle += angle;

        }
    }

}
