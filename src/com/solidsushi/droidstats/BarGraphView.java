package com.solidsushi.droidstats;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

class BarGraphView extends View
{
	private final static String TAG = BarGraphView.class.getSimpleName(); 
	
	// Graphics constants
	private static final int TEXT_AREA_PADDING = 100;
	
	/** Minimum with for text to be shown */
	private static final float MIN_WIDTH = 20;   
	
	private final int BAR_PADDING = 5;
	private final int MAX_BARS = 14;
	private final int BAR_TEXT_PADDING = 15;
	
	private String [] mTitles = null;
	private int [] mCount = null;
	private int mMax = Integer.MIN_VALUE;
	
	private Paint mTextPaint,mBarTextPaint,mBarPaint,mAxisPaint;
	private int mTextHeight = 0;

	private RectF[] mBars;

	private Paint[] mPaints;

	private int mBaseBarColor;

	public BarGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextAlign(Paint.Align.RIGHT);
		mTextPaint.setTextSize(12);
		
		mBarTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
		mBarTextPaint.setColor(Color.WHITE);
		
		Paint.FontMetricsInt metrics =  mTextPaint.getFontMetricsInt();
		mTextHeight  = metrics.top + metrics.bottom;
		
		mBarPaint = new Paint();
		mBarPaint.setColor(Color.BLUE);
		
		mAxisPaint = new Paint();
		mAxisPaint.setColor(Color.GRAY);
	}
	
	public void setData(HashMap<String,Integer> data)
	{	
		mTitles = new String[data.size()];
		mCount = new int [data.size()];
		
		int i=0;
		
		mMax = Integer.MIN_VALUE;
        
		// Print the hashmap
        Iterator<String> iter = data.keySet().iterator();
        while(iter.hasNext()) 
        {
        	String name = (String)iter.next();
        	int count = data.get(name);
        	
        	mMax = Math.max(count, mMax);
        	
        	mTitles[i] = nameMap(name);
        	mCount[i] = count;
        	
        	Log.v(TAG,"Adding " + name + "," + count);
        	
        	// I write bad code :-(
        	if(i == MAX_BARS) break;
        	i++;
        }
        
        Log.v(TAG,"MAX = "+ mMax);

        constructBars();
		invalidate();
	}
	
	public void onSizeChanged(int w, int h, int ow, int oh)
	{
		super.onSizeChanged(w, h, ow, oh);
		constructBars();
		invalidate();
	}
	
	private void constructBars()
	{
        if( mTitles != null && mTitles.length != 0){
	       
			int n = mTitles.length;
			
			mPaints = new Paint[n];
			mBars = new RectF[n];
			
			int barHeight = (getHeight()) / n;
		
			float  barTickWidth =  (getWidth() - TEXT_AREA_PADDING)/(float)mMax;
			
			Log.v(TAG, "N= " + n + " barHeight=" + barHeight + "tickWidth= " + barTickWidth + " max =" + mMax + " width=" + getWidth());
			
			// Draw every bar
			for(int i=0;i<n;i++)
			{
				RectF r = new RectF(	TEXT_AREA_PADDING,i*barHeight + BAR_PADDING, 
										TEXT_AREA_PADDING + mCount[i]*barTickWidth, (i+1)*barHeight);
			
				mBars[i] = r;		
				mPaints[i] = ColorFactory.getPaintPercent(mBaseBarColor, mCount[i], mMax);
			}
			
        }
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{		
		if(mBars != null)
		{
			int height = getHeight();
			int n = mTitles.length; 
			float x=0,y=0;
			
			// Draw every bar
			for(int i=0;i<n;i++)
			{
				RectF r = mBars[i];
				canvas.drawRect(r,mPaints[i]);
			
				Log.v(TAG, "Drawing " + r);
				x = TEXT_AREA_PADDING-10;
				y = r.bottom -(r.bottom-r.top)/2-mTextHeight/2; 
				
				// Draw the Label
				canvas.drawText(mTitles[i],x, y,mTextPaint);
				
				// Draw the 
				if(r.width() > MIN_WIDTH) 
					 canvas.drawText(String.valueOf(mCount[i]),x+BAR_TEXT_PADDING,y,mBarTextPaint);
			
			}
			
			// Draw the axis
			canvas.drawLine(TEXT_AREA_PADDING, 0, TEXT_AREA_PADDING, height, mAxisPaint);
		}
	}

	public void setColor(int color) {
		mBaseBarColor = color;
	}
	
	public String nameMap(String string)
	{
		String r = string;
		
		int index = string.indexOf(" ");
		
		if(index != -1 && index > 0 && index < string.length())
		{
			String lastName = string.substring(index);
			String firstInitial = string.substring(0,1) + ". ";
			
			r = firstInitial + lastName;
		}
				
		return r;
	}
	
}