package com.solidsushi.droidstats;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.TextView;

public class MinutesActivity extends Activity
{
	private Cursor mCursor = null;
    HashMap<String,Integer> mMinData = null;

    private final static String TAG = StatsActivity.class.getSimpleName(); 
	    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outgoing_calls);
    }
    
    @Override
    public void onStart()
    {
    	super.onStart();
    	
	    TextView label = (TextView)findViewById(R.id.headerText);
	    
	    String filter;
	   
		filter = CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE;
		label.setText("Duration");
		label.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.sym_call_incoming, 0, 0, 0);
	
	    // Make the query. 
	    mCursor = managedQuery(	  CallLog.Calls.CONTENT_URI,
	                              null, // Which columns to return 
	                              filter,       // Which rows to return (all rows)
	                              null,       // Selection arguments (none)
	                              null);
	    
	    constructCountData(mCursor);
	    
	    BarGraphView barView = (BarGraphView)findViewById(R.id.barGraph);
	    barView.setColor(Color.BLUE);
	    barView.setData(mMinData);
    }
    
	private void constructCountData(Cursor cur)
    { 
    	mMinData = new HashMap<String,Integer>(); 
    	
        if (cur.moveToFirst()) {

        	String name,cachedNumber,phoneNumber;
        	
            int nameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME); 
            int phoneColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
            int cachedNumberColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL);
            int durationColumn = cur.getColumnIndex(CallLog.Calls.DURATION);
            
            do {
                // Get the field values
                name = cur.getString(nameColumn);
                phoneNumber = cur.getString(phoneColumn);
                cachedNumber = cur.getString(cachedNumberColumn);
                int duration = cur.getInt(durationColumn);
                	
                // Hacky code Ralph
                if(name == null) name = "Unknown";
                
                if(!mMinData.containsKey(name)){
                	mMinData.put(name,duration);
                }
                else{
                	int data = mMinData.get(name) + duration;
                	Log.v(TAG,"Data: " + data);
                	mMinData.put(name, data);
                }
                
            } while (cur.moveToNext());
        }
        
        Log.v(TAG, "MAP " + mMinData);
    }
 }