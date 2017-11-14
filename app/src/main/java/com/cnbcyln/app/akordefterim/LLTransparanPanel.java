package com.cnbcyln.app.akordefterim;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LLTransparanPanel extends LinearLayout {
    private int Alpha;

    public LLTransparanPanel(Context context) {
        super(context);
    }

    public LLTransparanPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LLTransparanPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(final AttributeSet attrs) {
        Alpha = attrs.getAttributeIntValue(null, "Alpha", 150);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint innerPaint = new Paint();
        Paint borderPaint = new Paint();

        RectF drawRect = new RectF();
        drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());

        innerPaint.setARGB(Alpha, 0, 0, 0);

        // borderPaint.setARGB(255, 255, 255, 255);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1);

        canvas.drawRoundRect(drawRect, 10, 10, innerPaint);
        canvas.drawRoundRect(drawRect, 10, 10, borderPaint);

        super.dispatchDraw(canvas);
    }
}