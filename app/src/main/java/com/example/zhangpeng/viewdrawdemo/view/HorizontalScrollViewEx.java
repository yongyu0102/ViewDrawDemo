package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
/**
 * description: 实现水平方向的 ViewPaper 功能
 * author：pz
 * 时间：2016/12/8 :22:57
 */
public class HorizontalScrollViewEx extends ViewGroup {
    private static final String TAG = "HorizontalScrollViewEx";

    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (mScroller == null) {
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    /**
     * 解决滑动冲突
     * @param event 点击事件
     * @return 表明事件是否被拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            intercepted = false;
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
                intercepted = true;
            }
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastXIntercept;
            int deltaY = y - mLastYIntercept;
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                intercepted = true;
            } else {
                intercepted = false;
            }
            break;
        }
        case MotionEvent.ACTION_UP: {
            intercepted = false;
            break;
        }
        default:
            break;
        }

        Log.d(TAG, "intercepted=" + intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;

        return intercepted;
    }

    /**
     * 进行滑动处理
     * @param event 点击事件
     * @return 表明是否消费
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            break;
        }
        case MotionEvent.ACTION_MOVE: {
            int deltaX = x - mLastX;
            int deltaY = y - mLastY;
            scrollBy(-deltaX, 0);
            break;
        }
        case MotionEvent.ACTION_UP: {
            int scrollX = getScrollX();
            mVelocityTracker.computeCurrentVelocity(1000);
            float xVelocity = mVelocityTracker.getXVelocity();
            if (Math.abs(xVelocity) >= 50) {
                mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
            } else {
                mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
            }
            mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
            int dx = mChildIndex * mChildWidth - scrollX;
            smoothScrollBy(dx, 0);
            mVelocityTracker.clear();
            break;
        }
        default:
            break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    /**
     * 重写 onMeasure 方法，处理自定义 View 支持 wrap_content 模式
     * @param widthMeasureSpec 父容器给定宽度约束条件
     * @param heightMeasureSpec 父容器给定高度约束条件
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = 0;
        int measuredHeight = 0;
        //获取子视图个数
        final int childCount = getChildCount();
        //测量子视图
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //获取父容器给定获取测量模式和测量值
        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            //如果没有子视图直接设定 View 的宽/高为0
            setMeasuredDimension(0, 0);
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            //如果视图宽/高都采用 wrap_content 模式
            final View childView = getChildAt(0);
            //宽度为第一个视图宽度乘以所有子视图个数
            measuredWidth = childView.getMeasuredWidth() * childCount;
           // 那么高度为第一个视图宽度
            measuredHeight = childView.getMeasuredHeight();
            //设置 自定义视图宽/高值
            setMeasuredDimension(measuredWidth, measuredHeight);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            //如果只有视图高采用 wrap_content 模式
            final View childView = getChildAt(0);
            //设置视图高度为第一个视图高度
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize, childView.getMeasuredHeight());
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //如果视图宽度使用 wrap_content 模式，设置宽度为第一个视图宽度乘以所有子视图个数
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measuredWidth, heightSpaceSize);
        }
    }

    /**
     * 重写 onLayout 方法，实现摆放子视图功能
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //记录子视图左边距位置
        int childLeft = 0;
        //获取子视图个数
        final int childCount = getChildCount();
        //记录子视图个数
        mChildrenSize = childCount;
        //遍历子视图
        for (int i = 0; i < childCount; i++) {
            //获取子视图
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                //如果子视图可见，获取子视图测量宽度
                final int childWidth = childView.getMeasuredWidth();
                //记录子视图宽度
                mChildWidth = childWidth;
                //设置摆放子视图位置，每次子视图放置在上一个子视图右边依次排放
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
