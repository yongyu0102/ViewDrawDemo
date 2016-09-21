package com.example.zhangpeng.viewdrawdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.zhangpeng.viewdrawdemo.R;

/**
 * 自定义一个圆形图，支持 wrap_content 和 padding 属性
 * Created by zhangpeng on 2016/9/11.
 */
public class MyCircleView extends View {
    //自定义圆的颜色
    private Paint paint;
    private String text="未获取到指定内容";
    private int color= Color.RED;
    public MyCircleView(Context context) {
        super(context);
        init();
    }

    public MyCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    /**
     * 在构造方法中解析我们已经自己定义的属性并根据需要进行相应的处理
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MyCircleView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(final Context context, AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyCircleView);
        //获取自定义属性颜色值
        color=typedArray.getColor(R.styleable.MyCircleView_circle_color, Color.RED);
        //获取自定义的文字内容
        text=typedArray.getString(R.styleable.MyCircleView_circle_text);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            }
        },5000);
        typedArray.recycle();
    }

    private void init(){
        //初始化画笔
        paint=new Paint();
        //为画笔设置颜色
        paint.setColor(color);
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

    /**
     * 这里进行绘制 View 的内容，这里要注意需要处理 padding 值，
     * 让自定义的 View 支持 padding 属性，如果不处理， 那么在 xml 文件中使用该自定义的 View padding
     * 属性时候，将会失效
     * @param canvas 画布
     */
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
        //确定x轴和y轴圆中心点位置，主要受paddingLeft和withFinal/2 影响
        //即受左侧偏移量和圆半径有关，与 RightPadding 无关
        canvas.drawCircle(paddingLeft+withFinal/2,paddingTop+heightFinal/2,radius,paint);

    }
}
