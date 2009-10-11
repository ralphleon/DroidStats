package com.solidsushi.droidstats;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.TextView;

public class CallsActivity extends Activity
{
	private Cursor mCursor = null;
    HashMap<String,Integer> mCountData = null;

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
	    
	    switch(getIntent().getIntExtra("Type",-1))
	    {
	    	case StatsActivity.INCOMING_TYPE:
	    		 filter = CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE;
	    		 label.setText("Incoming Calls");
	    		 label.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.sym_call_incoming, 0, 0, 0);
	    		 break;
	    	
	    	case StatsActivity.OUTGOING_TYPE:
	    		 filter = CallLog.Calls.TYPE + " = " + CallLog.Calls.OUTGOING_TYPE;
	    		 label.setText("Outgoing Calls");
	    		 label.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.sym_call_outgoing, 0, 0, 0);
	    		 break;
	    
	    	case StatsActivity.MISSED_TYPE:
	    		 filter = CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE;
	    		 label.setText("Missed Calls");
	    		 label.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.sym_call_missed, 0, 0, 0);
	    		 break;
	    		 
	    	default:
	    		filter = null;
	    		label.setText("Error");
	    			 
	    }
	    
	    // Make the query. 
	    mCursor = managedQuery(	  CallLog.Calls.CONTENT_URI,
	                              null, // Which columns to return 
	                              filter,       // Which rows to return (all rows)
	                              null,       // Selection arguments (none)
	                              null);
	    
	    constructCountData(mCursor);
	    
	    BarGraphView barView = (BarGraphView)findViewById(R.id.barGraph);
	    barView.setData(mCountData); 
    }
    
	private void constructCountData(Cursor cur)
    { 
    	mCountData = new HashMap<String,Integer>(); 
    	
        if (cur.moveToFirst()) {

        	String name,cachedNumber,phoneNumber;
        	
            int nameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME); 
            int phoneColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
            int cachedNumberColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL);
            
            do {
                // Get the field values
                name = cur.getString(nameColumn);
                phoneNumber = cur.getString(phoneColumn);
                cachedNumber = cur.getString(cachedNumberColumn);
                
                // Hacky code Ralph
                if(name == null) name = "Unknown";
                
                if(!mCountData.containsKey(name)){
                	mCountData.put(name,1);
                }
                else{
                	int count = mCountData.get(name);
                	mCountData.put(name, ++count);
                }
                
            } while (cur.moveToNext());

        }
        
        // Print the hashmap
        Iterator<String> iter = mCountData.keySet().iterator();
        while(iter.hasNext()) 
        {
        	String name = (String)iter.next();
        	int count = mCountData.get(name);
        }
    }
 }