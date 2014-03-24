/**
 * 
 */
package se.kth.mobsec.cryptmsg;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity for displaying old messages
 * @author Jasper
 *
 */
public class AllMessagesActivity extends Activity {

	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    
    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;
    
    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;
    
    private ArrayList<String> smsList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allmessages);
		smsList.clear();
		
		//Retrieve SMS from Inbox
		/*ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ), null, null, null, null);

		int indexBody = cursor.getColumnIndex( AllMessagesActivity.BODY );
		int indexAddr = cursor.getColumnIndex( AllMessagesActivity.ADDRESS );
		
		if ( indexBody < 0 || !cursor.moveToFirst() ) return;

		do
		{
			String str = "Sender: " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody );
			smsList.add( str );
		}
		while( cursor.moveToNext() );*/

		//retireve all stoed CaaS messages form the database
		smsList = DataBaseHelper.getInstance().getAllMsgs();
			
		ListView smsListView = (ListView) findViewById( R.id.SMSList );
		smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
	}
	
	/**
	 * Create menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menus, menu);
		return true;
	}

	/**
	 * Add functionality to menu buttons
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_tomain:
			startActivity(new Intent("luh.dcsec.ba.hahn.android.MAINACTIVITY"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
