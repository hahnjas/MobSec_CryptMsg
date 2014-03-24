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

	public static final String GATEWAY_NUMBER = "01786785562";

	private static final String INDICATOR_LASTMSG = "A";

	/**
	 * Counter used to avoid duplicate messages
	 */
	private static int counter = 0;

	// for testing only, hard coded key
	private static SecretKeySpec symmKey;

	/**
	 * Handles sending of messages.
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void sendSMS(Context context, String recipient,String secret, String message) {

		if (recipient.startsWith("46"))
			recipient = recipient.replaceFirst("46", "0");

		if (recipient.startsWith("+46"))
			recipient = "0" + recipient.substring(3);

		// generate symmetric key
		symmKey = new SecretKeySpec("0011223344556677".getBytes(), "AES");

		byte[] encrypted = new byte[80];
		byte[] textToEncrypt = message.getBytes();


		// encrypt message
		String encryptedBase64 = "";
		try {
			encryptedBase64 = reduceBase64(se.kth.mobsec.cryptmsg.CryptoClient
					.encryptOutgoingMessage(secret, message));
			Toast.makeText(context, "msg: " + encryptedBase64,
					Toast.LENGTH_SHORT).show();
			// Toast.makeText(context,"encrypted: " + encryptedBase64,
			// Toast.LENGTH_LONG * 2).show();

		} catch (Exception e){
			//TODO handle this
		}


		// add authentication hash and phone number of recipient
		String base64Message = encryptedBase64;

		SmsManager smsTransfer = SmsManager.getDefault();
		// smsTransfer.sendTextMessage(GATEWAY_NUMBER, null, base64Message,
		// null, null);
//		smsTransfer.sendTextMessage(recipient, null, message, null,
//				null);
		Toast.makeText(context, base64Message, Toast.LENGTH_LONG).show();

	}

	/**
	 * Asks the CaaS server, whether a phone number is registered as a CaaS user
	 * 
	 * @param phoneNo
	 */
	public static void requestUserInfo(String phoneNo) {
		SmsManager smsTransfer = SmsManager.getDefault();
		smsTransfer.sendTextMessage(GATEWAY_NUMBER, null, "isUser-" + phoneNo,
				null, null);
	}

	/**
	 * Adds "=" to the end of a Base64 String to make its length a multiple of
	 * 4;
	 * 
	 * @param msg
	 * @return
	 */
	private static String completeBase64(String msg) {
		msg = msg.trim();
		while (msg.length() % 4 != 0)
			msg += "=";
		return msg;
	}

	/**
	 * Removes multiple "="s at the end of a Bas64 encoded String and always
	 * puts at least one to at the end. This is needed to avoid problems when
	 * splitting up messages usgin the "=" sign.
	 * 
	 * @param msg
	 * @return
	 */
	private static String reduceBase64(String msg) {
		msg = msg.replaceAll("==", "");
		msg = msg.replaceAll("=", "");

		return msg.trim() + "=";
	}

	public static void receiveSms(Context context, String originator,
			String message) {
		// TODO Auto-generated method stub
		
	}
}
