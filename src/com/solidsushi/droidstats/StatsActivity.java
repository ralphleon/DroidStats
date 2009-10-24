package com.solidsushi.droidstats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class StatsActivity extends Activity implements OnItemClickListener
{   
	public final static int INCOMING_TYPE = 0;
	public final static int OUTGOING_TYPE = 1;
	public final static int MISSED_TYPE = 2, DURATION = 3;
	
    private final static String TAG = StatsActivity.class.getSimpleName(); 
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  	
        // Create the list
        String[] mStrings = new String[]{"Incoming Calls", "Outgoing Calls","Missed Calls","Call Duration"}; 

        ListView listView = (ListView)findViewById(R.id.menuList);
        
        // Create an ArrayAdapter, that will actually make the Strings above appear in the ListView 
        listView.setAdapter(new ArrayAdapter<String>(this, 
                         android.R.layout.simple_list_item_1, mStrings));
        
        listView.setOnItemClickListener(this);
    }

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		Intent i = null;
		
		switch(arg2)
		{
			case OUTGOING_TYPE:
			case MISSED_TYPE:
			case INCOMING_TYPE:
				i = new Intent(this,CallsActivity.class);
				i.putExtra("Type",arg2);			
				break;
				
			case DURATION:
				i = new Intent(this,MinutesActivity.class);
				break;
		}
		
		startActivity(i);
		
	}
}