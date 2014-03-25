package se.kth.mobsec.cryptmsg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Handles outgoing messages as well as further processing of incoming messages.
 * 
 * @author Jasper
 * 
 */
public class SmsTransfer {

	/**
	 * Handles sending of messages.
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static String sendSMS(Context context, String recipient,
			String secret, String message) {

		if (recipient.startsWith("46"))
			recipient = recipient.replaceFirst("46", "0");

		if (recipient.startsWith("+46"))
			recipient = "0" + recipient.substring(3);

		// encrypt message
		String encryptedBase64 = "";
		try {
			encryptedBase64 = CryptoClient
					.encryptOutgoingMessage(secret, message);
			Toast.makeText(context, "msg: " + encryptedBase64,
					Toast.LENGTH_SHORT).show();
			// Toast.makeText(context,"encrypted: " + encryptedBase64,

		} catch (Exception e) {
			// TODO handle this
		}

		// add authentication hash and phone number of recipient
		String base64Message = encryptedBase64;

		SmsManager smsTransfer = SmsManager.getDefault();
		// send off the message
		 smsTransfer.sendTextMessage(recipient, null, message, null,
		 null);
		
		return base64Message;

	}

}
