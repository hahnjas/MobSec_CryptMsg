package se.kth.mobsec.cryptmsg;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	
	/**
	 * The Key
	 */
	private SecretKeySpec symmKey;
	/**
	 * initialization vector for encryption
	 */
	private IvParameterSpec iv;
	
	private int counter = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		counter++;
		

		// ---get the SMS message passed in---
		
		Bundle bundle = intent.getExtras();
		
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			//Toast.makeText(context, "New Message(s) received (" + msgs.length + " total)" + " - Counter: " + counter, Toast.LENGTH_SHORT).show();
			//for (int i = 0; i < msgs.length; i++) {
			int i = 0;
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				String msgText=msgs[i].getMessageBody();
				
				
				//	msgText = decrypt(context, msgs[i].getMessageBody());
				
				str += msgText;
				str += "\n";
				
				String originator = msgs[i].getOriginatingAddress();
				String message = msgs[i].getMessageBody();
				//make sure originating phone number has correct format
				if(originator.startsWith("49"))
					originator = originator.replaceFirst("49", "0");
				
				if(originator.startsWith("+49"))
					originator = "0" + originator.substring(3);
				
				if(originator.equals(SmsTransfer.GATEWAY_NUMBER) || message.startsWith("@CaaS")){ //FIXME use regular expression, e.g.:("*=*=*==")
					SmsTransfer.receiveSms(context, originator, message);
					Toast.makeText(context, "New CaaS message(s) received (" + msgs.length + " total)" + " - Counter: " + counter, Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(context, "unencrypted message: " + message, Toast.LENGTH_SHORT).show();
					
			//}
			// ---display the new SMS message---
			//Toast.makeText(context, "decrypted: " + str, Toast.LENGTH_SHORT).show();
			
			
		}
	}

	public String encrypt(Context context, String message) throws NoSuchAlgorithmException,
			NoSuchPaddingException, BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException {
		byte[] messageBytes = Base64.decode(message, Base64.DEFAULT);
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		try {
			cipher.init(Cipher.ENCRYPT_MODE, getKey(), this.iv);
		} catch (InvalidAlgorithmParameterException e) {
			// can't occur here since IV has fixed value
			e.printStackTrace();
		}
		byte[] encryptedMessage = cipher.doFinal(messageBytes);
		return Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
	}

	public String decrypt(Context context, String message){
		//Initialize the IV with zeros
		byte[] ivBytes = new byte[16];
		int a = 0;
		while(a<16){
			ivBytes[a] = 0;
			a++;
		}
		this.iv = new IvParameterSpec(ivBytes);
		
		byte[] decryptedMessage = "failure".getBytes();
		
		try {
			byte[] messageBytes = Base64.decode(message, Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, getKey(), this.iv);
			decryptedMessage = cipher.doFinal(messageBytes);
		} catch (InvalidAlgorithmParameterException e) {
			// can't occur here since IV has fixed value
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new String(decryptedMessage);//Base64.encodeToString(decryptedMessage, Base64.DEFAULT);
	}
	
	/**
	 * Returns the encryption key.
	 * 
	 * @return The encryption key.
	 */
	public SecretKeySpec getKey() {
		return this.symmKey;
	}

}