package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gcssloop.view.CustomView;

/**
 * 自定义遥控器界面
 * Created by zhangpeng on 2016/12/18.
 */

public class RemoteControlMenu extends CustomView {
    //定义绘制路径
    Path up_p, down_p, left_p, right_p, center_p;
    //记录绘制区域
    Region up, down, left, right, center;

    //转换矩阵
    Matrix mMapMatrix = null;
//标记点击位置
    int CENTER = 0;
    int UP = 1;
    int RIGHT = 2;
    int DOWN = 3;
    int LEFT = 4;
    int touchFlag = -1;
    int currentFlag = -1;

    //按钮监听器
    MenuListener mListener = null;

    int mDefauColor = 0xFF4E5268;
    int mTouchedColor = 0xFFDF9C81;


    public RemoteControlMenu(Context context) {
        this(context, null);
    }

    public RemoteControlMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        up_p = new Path();
        down_p = new Path();
        left_p = new Path();
        right_p = new Path();
        center_p = new Path();

        up = new Region();
        down = new Region();
        left = new Region();
        right = new Region();
        center = new Region();

        mDeafultPaint.setColor(mDefauColor);
        mDeafultPaint.setAntiAlias(true);

        mMapMatrix = new Matrix();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMapMatrix.reset();

        // 注意这个区域的大小
        Region globalRegion = new Region(-w, -h, w, h);
        //取整个 View的高宽最小值*0.8作为绘制按钮的区域
        int minWidth = w > h ? h : w;
        minWidth *= 0.8;

        int br = minWidth / 2;
        //取绘制区域的一半计算整个绘制区域，即外圈大圆显示区域
        RectF bigCircle = new RectF(-br, -br, br, br);
        //取绘制区域的1/4计算内圆绘制区域
        int sr = minWidth / 4;
        RectF smallCircle = new RectF(-sr, -sr, sr, sr);
        //大小圆扫面角度
        float bigSweepAngle = 84;
        //小内部小圆扫描角度
        float smallSweepAngle = -80;

        // 根据视图大小，初始化 Path 和 Region
        //取内部圆一半作为内部中心按钮绘制区域
        center_p.addCircle(0, 0, 0.2f * minWidth, Path.Direction.CW);
        //存储中心按钮区域
        center.setPath(center_p, globalRegion);

        //计算右边按钮绘制区域，以大圆为基准，开始角度 -40，技术扫描角度 84
        //画右边按钮大圆弧
        right_p.addArc(bigCircle, -40, bigSweepAngle);
        //画右边按钮小圆弧
        right_p.arcTo(smallCircle, 40, smallSweepAngle);
        //闭合绘制区域
        right_p.close();
        //存储右边按钮区域
        right.setPath(right_p, globalRegion);
        //每次大圆扫描角度为 84 ，而每次开始角度间隔50-（-40）=90
        //故每个按钮间隔留白区域为90-84=6 度空间，小圆计算方式同理
        down_p.addArc(bigCircle, 50, bigSweepAngle);
        down_p.arcTo(smallCircle, 130, smallSweepAngle);
        down_p.close();
        down.setPath(down_p, globalRegion);

        left_p.addArc(bigCircle, 140, bigSweepAngle);
        left_p.arcTo(smallCircle, 220, smallSweepAngle);
        left_p.close();
        left.setPath(left_p, globalRegion);

        up_p.addArc(bigCircle, 230, bigSweepAngle);
        up_p.arcTo(smallCircle, 310, smallSweepAngle);
        up_p.close();
        up.setPath(up_p, globalRegion);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pts = new float[2];
        //获取触摸点在屏幕上位置
        pts[0] = event.getRawX();
        pts[1] = event.getRawY();
        //转换为画布坐标
        mMapMatrix.mapPoints(pts);

        int x = (int) pts[0];
        int y = (int) pts[1];

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //获取触摸位置所在区域
                touchFlag = getTouchedPath(x, y);
                currentFlag = touchFlag;
                break;
            case MotionEvent.ACTION_MOVE:
                currentFlag = getTouchedPath(x, y);
                break;
            case MotionEvent.ACTION_UP:
                currentFlag = getTouchedPath(x, y);
                //设置点击事件
                // 如果手指按下区域和抬起区域相同且不为空，则判断点击事件
                if (currentFlag == touchFlag && currentFlag != -1 && mListener != null) {
                    if (currentFlag == CENTER) {
                        mListener.onCenterCliched();
                    } else if (currentFlag == UP) {
                        mListener.onUpCliched();
                    } else if (currentFlag == RIGHT) {
                        mListener.onRightCliched();
                    } else if (currentFlag == DOWN) {
                        mListener.onDownCliched();
                    } else if (currentFlag == LEFT) {
                        mListener.onLeftCliched();
                    }
                }
                //状态回归
                touchFlag = currentFlag = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                touchFlag = currentFlag = -1;
                break;
        }
        //只要点击该 view 就进行重新绘制

        invalidate();
        return true;
    }

    // 获取当前触摸点在哪个区域
    int getTouchedPath(int x, int y) {
        if (center.contains(x, y)) {
            return 0;
        } else if (up.contains(x, y)) {
            return 1;
        } else if (right.contains(x, y)) {
            return 2;
        } else if (down.contains(x, y)) {
            return 3;
        } else if (left.contains(x, y)) {
            return 4;
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动画布坐标点到 view 中心位置
        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        // 获取测量矩阵(逆矩阵)
        if (mMapMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMapMatrix);
        }

        // 绘制默认颜色
        canvas.drawPath(center_p, mDeafultPaint);
        canvas.drawPath(up_p, mDeafultPaint);
        canvas.drawPath(right_p, mDeafultPaint);
        canvas.drawPath(down_p, mDeafultPaint);
        canvas.drawPath(left_p, mDeafultPaint);

        // 绘制触摸区域颜色
        mDeafultPaint.setColor(mTouchedColor);
        if (currentFlag == CENTER) {
            canvas.drawPath(center_p, mDeafultPaint);
        } else if (currentFlag == UP) {
            canvas.drawPath(up_p, mDeafultPaint);
        } else if (currentFlag == RIGHT) {
            canvas.drawPath(right_p, mDeafultPaint);
        } else if (currentFlag == DOWN) {
            canvas.drawPath(down_p, mDeafultPaint);
        } else if (currentFlag == LEFT) {
            canvas.drawPath(left_p, mDeafultPaint);
        }
        mDeafultPaint.setColor(mDefauColor);
    }

    public void setListener(MenuListener listener) {
        mListener = listener;
    }

    // 点击事件监听器
    public interface MenuListener {
        void onCenterCliched();

        void onUpCliched();

        void onRightCliched();

        void onDownCliched();

        void onLeftCliched();
    }
}
