package se.kth.mobsec.cryptmsg;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DecryptMsgActivity extends Activity {

	Button btnDecryptSMS;
	Button btnAllSMS;
	Button btnContacts;
	Button btnReg;

	final int REQ_CODE = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.decrypt);

		// add functionality to send button
		btnDecryptSMS = (Button) findViewById(R.id.btnDecrypt);
		btnDecryptSMS.setOnClickListener(new View.OnClickListener() {
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
					Toast.makeText(getBaseContext(), "@string/decryptedMsg" + plainMsg,
							Toast.LENGTH_SHORT);
					secretField.setEnabled(false);
					btnDecryptSMS.setEnabled(false);
					text.setText(plainMsg);
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