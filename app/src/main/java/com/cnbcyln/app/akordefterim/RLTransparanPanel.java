package com.cnbcyln.app.akordefterim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RLTransparanPanel extends RelativeLayout {

	public RLTransparanPanel(Context context)
	{
		super(context);
	}
	
	public RLTransparanPanel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		Paint innerPaint = new Paint();
		Paint borderPaint = new Paint();
		
		RectF drawRect = new RectF();
		drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
		
		innerPaint.setARGB(150,0,0,0);
		
		//borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(1);
		
		canvas.drawRoundRect(drawRect, 10, 10, innerPaint);
		canvas.drawRoundRect(drawRect, 10, 10, borderPaint);
		
		super.dispatchDraw(canvas);
	}
}