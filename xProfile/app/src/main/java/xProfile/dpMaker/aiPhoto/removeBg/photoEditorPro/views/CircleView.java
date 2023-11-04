package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;

public class CircleView extends View {


    private static int DEFAULT_STROKE_COLOR = Color.RED;
    private static int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static int DEFAULT_FILL_COLOR = Color.BLUE;

    private static float DEFAULT_STROKE_WIDTH = 5f;
    private static float DEFAULT_FILL_RADIUS = 0.9f;

    private static final int DEFAULT_VIEW_SIZE = 96;

    private int mStrokeColor = DEFAULT_STROKE_COLOR;
    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int mFillColor = DEFAULT_FILL_COLOR;

    private float mStrokeWidth = DEFAULT_STROKE_WIDTH;
    private float mFillRadius = DEFAULT_FILL_RADIUS;


    private Paint mStrokePaint;
    private Paint mBackgroundPaint;
    private Paint mFillPaint;

    private RectF mInnerRectF;

    private int mViewSize;

    public CircleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CircleView, defStyle, 0);

        mBackgroundColor = a.getColor(R.styleable.CircleView_cv_backgroundColorValue,DEFAULT_BACKGROUND_COLOR);
        mStrokeColor = a.getColor(R.styleable.CircleView_cv_strokeColorValue,DEFAULT_STROKE_COLOR);
        mFillColor = a.getColor(R.styleable.CircleView_cv_fillColor,DEFAULT_FILL_COLOR);

        mStrokeWidth = a.getFloat(R.styleable.CircleView_cv_strokeWidthSize,DEFAULT_STROKE_WIDTH);
        mFillRadius = a.getFloat(R.styleable.CircleView_cv_fillRadius,DEFAULT_FILL_RADIUS);

        a.recycle();

        //Stroke Paint
        mStrokePaint = new Paint();
        mStrokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(mStrokeColor);
        mStrokePaint.setStrokeWidth(mStrokeWidth);

        //Background Paint
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);

        //Fill Paint
        mFillPaint = new Paint();
        mFillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(mFillColor);

        mInnerRectF = new RectF();

    }

    private void invalidatePaints(){
        mBackgroundPaint.setColor(mBackgroundColor);
        mStrokePaint.setColor(mStrokeColor);
        mFillPaint.setColor(mFillColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = resolveSize(DEFAULT_VIEW_SIZE, widthMeasureSpec);
        int height = resolveSize(DEFAULT_VIEW_SIZE, heightMeasureSpec);
        mViewSize = Math.min(width, height);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mInnerRectF.set(0, 0, mViewSize, mViewSize);
        mInnerRectF.offset((getWidth() - mViewSize) / 2, (getHeight() - mViewSize) / 2);

        final int halfBorder = (int) (mStrokePaint.getStrokeWidth() / 2f + 0.5f);

        mInnerRectF.inset(halfBorder, halfBorder);

        float centerX = mInnerRectF.centerX();
        float centerY = mInnerRectF.centerY();

        canvas.drawArc(mInnerRectF, 0, 360, true, mBackgroundPaint);

        float radius = (mViewSize / 2) * mFillRadius;

        canvas.drawCircle(centerX, centerY, radius + 0.5f - mStrokePaint.getStrokeWidth(), mFillPaint);
        canvas.drawOval(mInnerRectF, mStrokePaint);

    }
    public int getStrokeColor() {
        return mStrokeColor;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getFillColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        invalidatePaints();
    }
    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        invalidatePaints();
    }

    public void setFillColor(int fillColor) {
        mFillColor = fillColor;
        invalidatePaints();
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setBackgroundColor(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }
    public float getFillRadius() {
        return mFillRadius;
    }


    public void setFillRadius(float fillRadius) {
        mFillRadius = fillRadius;
        invalidate();
    }

}