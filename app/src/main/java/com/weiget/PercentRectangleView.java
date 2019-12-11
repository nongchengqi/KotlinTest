package com.weiget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class PercentRectangleView extends View {
    private Paint mTotalPaint;
    private Paint mStrokePaint;
    private Paint mUsedPaint;
    private Paint mTextPaint;

    private float mStrokeWidth = 0f, mStrokeRadius = 10f;
    private float mPercent = 0.3f;
    private float mTextSizePercent = 0.45f;//文字大小占控件高度的多少
    private CharSequence mTips;
    private CharSequence[] mTipArr;

    private int mTotalColor = Color.parseColor("#38CE77");
    private int mUsedColor = Color.parseColor("#FFB13C");
    private int mUsedTextColor = Color.parseColor("#FFFFFF");
    private int mTotalTextColor = Color.parseColor("#FFFFFF");
    private int mStrokeColor = Color.parseColor("#333333");

    private RectF mTotalRectF, mUsedRectF;

    public PercentRectangleView(Context context) {
        this(context, null);
    }

    public PercentRectangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentRectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTotalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mTotalPaint.setStyle(Paint.Style.STROKE);//空心矩形框
        mTotalPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mTotalPaint.setColor(mTotalColor);

        mUsedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mUsedPaint.setStyle(Paint.Style.STROKE);//空心矩形框
        mUsedPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mUsedPaint.setColor(mUsedColor);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);


        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);//空心矩形框
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setColor(mStrokeColor);


        mTotalRectF = new RectF();
        mUsedRectF = new RectF();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalRectF.set(getPaddingLeft(), getPaddingTop(), w - getPaddingEnd(), h - getPaddingBottom());
        mUsedRectF.set(getPaddingLeft(), getPaddingTop(), (w - getPaddingEnd()) * mPercent, h - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制总容量
        canvas.drawRoundRect(mTotalRectF, mStrokeRadius, mStrokeRadius, mTotalPaint);
        //绘制已使用容量
        mUsedRectF.set(mTotalRectF.left, mTotalRectF.top, (mTotalRectF.right) * mPercent, mTotalRectF.bottom);
        canvas.drawRoundRect(mUsedRectF, mStrokeRadius, mStrokeRadius, mUsedPaint);
        //绘制边框
        if (mStrokeWidth>0) {
            canvas.drawRoundRect(mTotalRectF, mStrokeRadius, mStrokeRadius, mStrokePaint);
        }
        //文字绘制
        if (!TextUtils.isEmpty(mTips)) {
            mTextPaint.setColor(mTotalTextColor);
            mTextPaint.setTextSize(mTotalRectF.height() * mTextSizePercent);
            float w = mTotalRectF.width() - mTextPaint.measureText(mTips, 0, mTips.length());
            canvas.drawText(mTips, 0, mTips.length(), w / 2, mTotalRectF.height() / (2 - mTextSizePercent), mTextPaint);
        }else if (mTipArr!=null){
            float drawWidth=0f;
            float textWidth=0f;
            mTextPaint.setTextSize(mTotalRectF.height() * mTextSizePercent);
            //先绘制已使用
            mTextPaint.setColor(mUsedTextColor);
            textWidth = mTextPaint.measureText(mTipArr[0], 0, mTipArr[0].length());
            drawWidth = mUsedRectF.width() - textWidth;
            if (drawWidth<=0) drawWidth=4;//已使用容量面积不足以显示文字 只能到未使用区域穿透显示
            canvas.drawText(mTipArr[0], 0, mTipArr[0].length(), drawWidth / 2, mUsedRectF.height() / (2 - mTextSizePercent), mTextPaint);
            //绘制未使用
            mTextPaint.setColor(mTotalTextColor);
            textWidth = mTextPaint.measureText(mTipArr[1], 0, mTipArr[1].length());
            //需要减去已使用的范围
            drawWidth = mTotalRectF.width()- mUsedRectF.width() - textWidth;
            if (drawWidth <0){
                //剩余总容量面积不足以显示文字 只能穿透到已使用区域显示
                drawWidth=drawWidth*2;
            }

            canvas.drawText(mTipArr[1], 0, mTipArr[1].length(), mUsedRectF.width()+drawWidth / 2, mTotalRectF.height() / (2 - mTextSizePercent), mTextPaint);
        }
    }

    /**设置总容量的颜色*/
    public PercentRectangleView setTotalColor(int color) {
        mTotalColor = color;
        mTotalPaint.setColor(mTotalColor);
        return this;
    }

    /**设置已使用容量的颜色*/
    public PercentRectangleView setUsedColor(int color) {
        mUsedColor = color;
        mUsedPaint.setColor(mUsedColor);
        return this;
    }

    /**设置已使用容量的字体颜色*/
    public PercentRectangleView setUsedTextColor(int color) {
        mUsedTextColor = color;
        return this;
    }

    /**设置总容量的字体颜色*/
    public PercentRectangleView setTotalTextColor(int color) {
        mTotalTextColor = color;
        return this;
    }

    /**设置总容量的字体颜色*/
    public PercentRectangleView setStrokeColor(int color) {
        mTotalPaint.setColor(color);
        return this;
    }

    /**设置进度条边框  以及圆角*/
    public PercentRectangleView setStroke(float width, float radius) {
        mStrokeWidth = width;
        mStrokeRadius = radius;
        mStrokePaint.setStrokeWidth(width);
        return this;
    }

    /**更新当前进度*/
    public PercentRectangleView setPercent(float percent) {
        mPercent = percent;
        return this;
    }

    /**进度内容显示*/
    public PercentRectangleView setTips(CharSequence tips) {
        mTips = tips;
        mTipArr = null;
        return this;
    }
    /**进度内容显示   区分显示*/
    public PercentRectangleView setTipArray(@NonNull CharSequence used, @NonNull CharSequence total) {
        if (mTipArr==null){
            mTipArr = new CharSequence[2];
        }
        mTipArr[0] = used;
        mTipArr[1] =total;
        return this;
    }

    /**字体大小占空间高度的百分比 */
    public PercentRectangleView setTestSizePercent(float sizePercent) {
        mTextSizePercent = sizePercent;
        return this;
    }

    /**为了避免频繁刷新，更新属性后记得调用此方法*/
    public void update() {
        invalidate();
    }

    /**使得改View支持  wrap_content*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = 800;
        int mHeight = 80;

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }
}
