package goo.TeaTimer;


import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.view.MenuItem;

public class TimerPrefActivity extends PreferenceActivity 
{
	private static final String TAG = TimerPrefActivity.class.getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable to touch the icon in the ActionBar to get back to the main activity
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        // Load the sounds
        ListPreference tone = (ListPreference)findPreference("NotificationUri");
        	
    	String [] cols = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE};
    	
    	// Lets check out the media provider
        Cursor cursor = managedQuery(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, 
        		cols, 
        		"is_ringtone OR is_notification",
        		null, "is_ringtone, title_key");
         int i=0;
   
         CharSequence[] items = null;
         CharSequence[] itemUris = null;
         
        if(cursor != null && cursor.getCount() > 0){
        	
            items = new CharSequence[cursor.getCount()];
            itemUris = new CharSequence[cursor.getCount()];
        
        	int colTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);;
        	int colId = cursor.getColumnIndex(MediaStore.Audio.Media._ID);;
	        
        	while(!cursor.isLast()){
	        	cursor.moveToNext();
	        	items[i] = cursor.getString(colTitle);
	        	itemUris[i] = MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "/" + cursor.getLong(colId);
	        	i++;
        	}
        
	        cursor.close();   
		}
        
    	CharSequence [] entries = {"No Sound","Big Ben"};
    	CharSequence [] entryValues = {"","android.resource://goo.TeaTimer/" + R.raw.big_ben};
    	
    	//Default value
    	if(tone.getValue() == null) tone.setValue((String)entryValues[1]);
    	
    	if( items != null && items.length > 0){
    		tone.setEntries(concat(entries,items));
    		tone.setEntryValues(concat(entryValues,itemUris));
    	}else{
    		tone.setEntries(entries);
    		tone.setEntryValues(entryValues);
    	}
    	
    }
    
    static private CharSequence [] concat( CharSequence[] A, CharSequence[] B) 
    {		
    	CharSequence[] C= new CharSequence[A.length+B.length];
    	System.arraycopy(A, 0, C, 0, A.length);
    	System.arraycopy(B, 0, C, A.length, B.length);

    	   return C;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// app icon in action bar clicked; go home
				Intent intent = new Intent(this, TimerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}