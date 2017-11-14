package com.cnbcyln.app.akordefterim.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class ShadowImage extends ImageView {

    static float DENSITY = 1f;
    static final float SHADOW_DISTANCE = 10f;
    static final int SHADOW_COLOR = 0xAA000000;

    Path path;
    Paint paint;

    Point p1, p2, p3;

    public ShadowImage(Context context) {
        this(context, null);
    }

    public ShadowImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(@NonNull Context context) {
        DENSITY = context.getResources().getDisplayMetrics().density;

        path = new Path();
        paint = new Paint();
        p1 = new Point();
        p2 = new Point();
        p3 = new Point();

        // Required to make the ShadowLayer work properly
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    void updateDrawVariables() {
        int shadowSize = (int)(SHADOW_DISTANCE * DENSITY);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setShadowLayer(shadowSize, 0, -1, SHADOW_COLOR);

        // Offset the actual position by the shadow size so
        int left = 0 - shadowSize;
        int right = getMeasuredWidth() + shadowSize;
        int bottom = getMeasuredHeight();

        p1.set(left, bottom);
        p2.set(right, bottom - (int)(52 * DENSITY));
        p3.set(right, bottom);

        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        // Move the path shape down so that the shadow doesn't "fade" at the left and right edges
        path.lineTo(p3.x, p3.y + shadowSize);
        path.lineTo(p1.x, p1.y + shadowSize);
        path.close();
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Update all the drawing variables if the layout values have changed
        if(changed) {
            updateDrawVariables();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Paint the current path values that were set after onLayout()
        canvas.drawPath(path, paint);
    }
}