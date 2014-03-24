/**
 * 
 */
package se.kth.mobsec.cryptmsg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Jasper
 * 
 */
public class SetSecretActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setsecret);
		
		// add functionality to OK button
		Button btnSetSec = (Button) findViewById(R.id.btnSetSecret);
		btnSetSec.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				EditText secretField = (EditText) findViewById(R.id.secret);
				String newSecret = secretField.getText().toString();
				CryptoClient.setSecret(newSecret);
				
				Toast.makeText(getBaseContext(), "@string/succSecretSet", Toast.LENGTH_SHORT).show();
			}});
	}
}
