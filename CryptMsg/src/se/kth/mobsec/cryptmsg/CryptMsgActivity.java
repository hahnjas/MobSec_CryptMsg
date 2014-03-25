package se.kth.mobsec.cryptmsg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CryptMsgActivity extends Activity {

	Button btnSendSMS;
	Button btnAllSMS;
	Button btnContacts;
	Button btnReg;

	final int REQ_CODE = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// SmsTransfer transfer = new SmsTransfer();

		
		super.onCreate(savedInstanceState);
		// register Receiver for binary SMS
		// this.binSmsReceiver = new BinarySMSReceiver();
		// registerReceiver(binSmsReceiver, new IntentFilter(
		// "android.intent.action.DATA_SMS_RECEIVED"));
		// register receiver for standard SMS
//		this.smsReceiver = new SMSReceiver();
//		registerReceiver(smsReceiver, new IntentFilter(
//				"android.provider.Telephony.SMS_RECEIVED"));

		setContentView(R.layout.main);

		// add functionality to send button
		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Send out an SMS message
				 */
				EditText phoneNo = (EditText) findViewById(R.id.editText1);
				EditText text = (EditText) findViewById(R.id.editText2);
				String noString = phoneNo.getText().toString();
				if (noString.isEmpty()) {
					Toast toast = Toast.makeText(getBaseContext(),
							R.string.noNumberSelected, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
					toast.show();
					return;
				}

				String secret = "secret"; //TODO retrieve secret from textfield
				EditText secretField = (EditText) findViewById(R.id.editSecret);
				secret = secretField.getText().toString();
				String encodedMsg = SmsTransfer.sendSMS(getBaseContext(), noString, secret, text.getText()
						.toString());

				phoneNo.setText("");
				// text.setText("");

				Toast.makeText(getBaseContext(), "@string/sent" + encodedMsg,
						Toast.LENGTH_SHORT);
				
				text.setText(encodedMsg);

			}
		});

		// call contacts activity via intent
		btnContacts = (Button) findViewById(R.id.btnChooseContact);
		btnContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(android.content.Intent.ACTION_PICK);
				i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(i, REQ_CODE);
			}
		});

	}

	/**
	 * De-attach uneeded modules on application exit.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		case R.id.menu_decrypt:
			startActivity(new Intent(
					"luh.dcsec.ba.hahn.android.DECRYPTMSGACTIVITY"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Use contact information received via the intent to get the phoneNumber.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQ_CODE:

			final EditText phoneInput = (EditText) findViewById(R.id.editText1);
			Cursor cursor = null;
			String phoneNumber = "";
			List<String> allNumbers = new ArrayList<String>();
			int phoneIdx = 0;
			Uri result = data.getData();
			String id = result.getLastPathSegment();
			phoneInput
					.setText(this.getContactPhoneNumber(getBaseContext(), id));

			break;
		}
		{
			// activity result error actions
		}
	}

	/**
	 * Retrieves the Phone Number of a Contact
	 * 
	 * @param ctx
	 * @param contactId
	 * @return
	 */
	public String getContactPhoneNumber(Context ctx, String id) {
		Uri contact_uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		contact_uri = Uri.withAppendedPath(contact_uri, id);
		Cursor phones = ctx.getContentResolver().query(contact_uri, null, null,
				null, null);
		while (phones.moveToNext()) {
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			if (phoneNumber != null) {
				return phoneNumber;
			}
		}
		phones.close();
		return "@string/noNumber";
	}

}