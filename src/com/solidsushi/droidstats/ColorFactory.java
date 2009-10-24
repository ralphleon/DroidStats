package com.solidsushi.droidstats;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

class ColorFactory{
	
	private static final String TAG = ColorFactory.class.getSimpleName();
	
	public static Paint [] getSpectrum(int color,int n)
	{
		Paint [] paints = new Paint[n];
		
		for(int i=0;i<n;i++){
		
			paints[i] = getPaintPercent(color,i,n);
		}
		
		return paints;
	}
	
	public static Paint getPaintPercent(int color, int i, int n)
	{
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		if( n <= 0 ){
			Log.e(TAG,"Given an invalid max, " + n);
			return paint;
		}
		
		float p = i/(float)n * .5f + .5f;
		int r = (int)(Color.red(color)*p);
		int g = (int)(Color.green(color)*p);
		int b = (int)(Color.blue(color)*p);
		
		paint.setColor(Color.rgb(r, g, b));
		
		return paint;
	}
	
}