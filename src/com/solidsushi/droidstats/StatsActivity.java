package com.solidsushi.droidstats;

import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;

public class StatsActivity extends Activity 
{
    private Cursor mCursor = null;
    HashMap<String,Integer> mCountData = null;

    private final static String TAG = StatsActivity.class.getSimpleName(); 
    

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  	
	    // Get the base URI for the People table in the Contacts content provider.
	    Uri contacts =  CallLog.Calls.CONTENT_URI;
	
	    // Make the query. 
	    mCursor = managedQuery(	  contacts,
	                              null, // Which columns to return 
	                              null,       // Which rows to return (all rows)
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

        	String name; 
            String phoneNumber;
            int nameColumn = cur.getColumnIndex(CallLog.Calls.CACHED_NAME); 
            int phoneColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
            
            do {
                // Get the field values
                name = cur.getString(nameColumn);
                phoneNumber = cur.getString(phoneColumn);
               
                if(name == null) name = "Unknown";
                
                if(!mCountData.containsKey(name)){
                	Log.v(TAG,"Found a new dude = " + name);
                	mCountData.put(name,1);
                }
                else{
                	int count = mCountData.get(name);
                	mCountData.put(name, ++count);
                }
                
                Log.v("x","name: " +name + "#: " + phoneNumber);
                
            } while (cur.moveToNext());

        }
        
        // Print the hashmap
        Iterator<String> iter = mCountData.keySet().iterator();
        while(iter.hasNext()) 
        {
        	String name = (String)iter.next();
        	int count = mCountData.get(name);
        	
        	Log.v(TAG,"Name: " + name + " count " + count);
        }
     
    }
}