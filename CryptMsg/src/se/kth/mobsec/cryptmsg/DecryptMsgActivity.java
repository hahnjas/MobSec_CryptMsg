package se.kth.mobsec.cryptmsg;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class DecryptMsgActivity extends Activity {

	Button btnSendSMS;
	Button btnAllSMS;
	Button btnContacts;
	Button btnReg;

	final int REQ_CODE = 1;
	private BinarySMSReceiver binSmsReceiver;
	private SMSReceiver smsReceiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// SmsTransfer transfer = new SmsTransfer();

		
		DataBaseHelper dbHelper = new DataBaseHelper(getBaseContext());
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.decrypt);

		// add functionality to send button
		btnSendSMS = (Button) findViewById(R.id.btnDecrypt);
		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Send out an SMS message
				 */
				EditText text = (EditText) findViewById(R.id.editText2);

				String secret = "secret"; //TODO retrieve secret from textfield
				EditText secretField = (EditText) findViewById(R.id.editSecret);
				secret = secretField.getText().toString();
				String encryptedMessage = text.getText().toString();
				try {
					String plainMsg = CryptoClient.decryptIncomingMessage(secret, encryptedMessage);
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidAlgorithmParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(getBaseContext(), "@string/sent",
						Toast.LENGTH_SHORT);

			}
		});


	}

	/**
	 * De-attach uneeded modules on application exit.
	 */
	@Override
	protected void onDestroy() {
		//unregisterReceiver(binSmsReceiver);
		unregisterReceiver(smsReceiver);
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


}