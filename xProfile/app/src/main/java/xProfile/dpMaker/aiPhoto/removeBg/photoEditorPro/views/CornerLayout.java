package xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xProfile.dpMaker.aiPhoto.removeBg.photoEditorPro.R;


public class CornerLayout extends FrameLayout {
    private int clippedBackgroundColor;
    private int borderColor;
    private int softBorderColor;
    private float borderWidth;
    private float softBorderWidth = 1;
    private float cornerRadius;
    private float cornerRadiusTopLeft;
    private float cornerRadiusTopRight;
    private float cornerRadiusBottomRight;
    private float cornerRadiusBottomLeft;

    private Path borderPath;
    private Paint borderPaint;
    private RectF oval;

    public CornerLayout(@NonNull Context context) {
        super(context);

        initialize();
    }

    public CornerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadAttributes(context, attrs);

        initialize();
    }

    public CornerLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadAttributes(context, attrs);

        initialize();
    }

    private void loadAttributes(Context context, AttributeSet attrs) {

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CornerLayout);

        clippedBackgroundColor = attributes.getColor(R.styleable.CornerLayout_iBackground, Color.TRANSPARENT);
        borderColor = attributes.getColor(R.styleable.CornerLayout_StrokeColor, Color.TRANSPARENT);
        borderWidth = attributes.getDimension(R.styleable.CornerLayout_StrokeWidth, 0);
        softBorderColor = attributes.getColor(R.styleable.CornerLayout_iBorderColor, Color.TRANSPARENT);
        cornerRadius = attributes.getDimension(R.styleable.CornerLayout_Radius, -1);

        cornerRadiusTopLeft = attributes.getDimension(R.styleable.CornerLayout_TopLeft, 0);
        cornerRadiusTopRight = attributes.getDimension(R.styleable.CornerLayout_TopRight, 0);
        cornerRadiusBottomRight = attributes.getDimension(R.styleable.CornerLayout_BottomRight, 0);
        cornerRadiusBottomLeft = attributes.getDimension(R.styleable.CornerLayout_BottomLeft, 0);

        borderWidth = Math.max(0, borderWidth);

        // Valid cornerRadius has higher priority
        if (cornerRadius >= 0) {
            setAllCornerRadius();
        }

        attributes.recycle();
    }

    private void initialize() {

        borderPath = new Path();
        oval = new RectF();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);

        // ViewOutlineProvider does not support clipping customized path
        if (canUseViewOutlineProvider()) {
            ViewOutlineProvider provider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {

                    if (canUseViewOutlineProvider()) {
                        float radius = Math.max(0, cornerRadius);
                        outline.setRoundRect(0, 0, getWidth(), getHeight(), radius);
                    }
                }
            };

            setOutlineProvider(provider);
            setClipToOutline(true);
        }
    }

    private boolean canUseViewOutlineProvider() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean shouldUseViewOutlineProvider() {
        return cornerRadius >= 0 && canUseViewOutlineProvider();
    }

    @Override
    public void requestLayout() {
        super.requestLayout();

        if (canUseViewOutlineProvider()) {
            invalidateOutline();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        // Path for border in both cases
        configurePath(getWidth(), getHeight());

        // Use outline provider as possible
        if (!shouldUseViewOutlineProvider()) {
            canvas.clipPath(borderPath);
        }

        if (clippedBackgroundColor != Color.TRANSPARENT) {
            canvas.drawColor(clippedBackgroundColor);
        }

        super.dispatchDraw(canvas);

        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        canvas.drawPath(borderPath, borderPaint);

        if (softBorderColor != Color.TRANSPARENT) {

            borderPaint.setColor(softBorderColor);
            borderPaint.setStrokeWidth(softBorderWidth);
            canvas.drawPath(borderPath, borderPaint);
        }
    }

    private void configurePath (int width, int height) {
        borderPath.rewind();
        float[] cornerRadii = {cornerRadiusTopLeft, cornerRadiusTopLeft, cornerRadiusTopRight, cornerRadiusTopRight,
                cornerRadiusBottomRight, cornerRadiusBottomRight, cornerRadiusBottomLeft, cornerRadiusBottomLeft};
        borderPath.addRoundRect(new RectF(0, 0, width, height), cornerRadii, Path.Direction.CW);
    }

    private void setAllCornerRadius() {
        setAllCornerRadius(cornerRadius, cornerRadius, cornerRadius, cornerRadius);
    }

    public void setAllCornerRadius(int topLeft, int topRight, int bottomRight, int bottomLeft) {
        setAllCornerRadius(dpToPx(topLeft), dpToPx(topRight), dpToPx(bottomRight), dpToPx(bottomLeft));
    }

    private void setAllCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        if (topLeft < 0 || topRight < 0 || bottomRight < 0 || bottomLeft < 0) {
            return;
        }

        cornerRadiusTopLeft = topLeft;
        cornerRadiusTopRight = topRight;
        cornerRadiusBottomRight = bottomRight;
        cornerRadiusBottomLeft = bottomLeft;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return pxToDp(borderWidth);
    }

    public void setBorderWidth(int dp) {
        setBorderWidth(dpToPx(dp));
    }

    public void setBorderWidth(float px) {
        if (px >= 0) {
            this.borderWidth = px;
        }
    }

    public int getSoftBorderColor() {
        return softBorderColor;
    }

    public void setSoftBorderColor(int softBorderColor) {
        this.softBorderColor = softBorderColor;
    }

    public int getClippedBackgroundColor() {
        return clippedBackgroundColor;
    }

    public void iBackgroundColor(int clippedBackgroundColor) {
        this.clippedBackgroundColor = clippedBackgroundColor;
    }

    public int getCornerRadius() {
        return pxToDp(Math.max(0, cornerRadius));
    }

    public void setCornerRadius(int dp) {
        setCornerRadius(dpToPx(dp));
    }

    public void setCornerRadius(float px) {
        if (px >= 0) {
            this.cornerRadius = px;
            setAllCornerRadius();
        }
    }

    public int getCornerRadiusTopLeft() {
        return pxToDp(cornerRadiusTopLeft);
    }

    public void setCornerRadiusTopLeft(int dp) {
        setCornerRadiusTopLeft(dpToPx(dp));
    }

    public void setCornerRadiusTopLeft(float px) {
        if (px >= 0) {
            this.cornerRadiusTopLeft = px;
            cornerRadius = -1;
        }
    }

    public int getCornerRadiusTopRight() {
        return pxToDp(cornerRadiusTopRight);
    }

    public void setCornerRadiusTopRight(int dp) {
        setCornerRadiusTopRight(dpToPx(dp));
    }
    public void setCornerRadiusTopRight(float px) {
        if (px >= 0) {
            this.cornerRadiusTopRight = px;
            cornerRadius = -1;
        }
    }

    public int getCornerRadiusBottomRight() {
        return pxToDp(cornerRadiusBottomRight);
    }

    public void setCornerRadiusBottomRight(int dp) {
        setCornerRadiusBottomRight(dpToPx(dp));
    }

    public void setCornerRadiusBottomRight(float px) {
        if (px >= 0) {
            this.cornerRadiusBottomRight = px;
            cornerRadius = -1;
        }
    }

    public int getCornerRadiusBottomLeft() {
        return pxToDp(cornerRadiusBottomLeft);
    }

    public void setCornerRadiusBottomLeft(int dp) {
        setCornerRadiusBottomLeft(dpToPx(dp));
    }

    public void setCornerRadiusBottomLeft(float px) {
        if (px >= 0) {
            this.cornerRadiusBottomLeft = px;
            cornerRadius = -1;
        }
    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private int pxToDp(float px) {
        return (int) (px / getContext().getResources().getDisplayMetrics().density);
    }
}