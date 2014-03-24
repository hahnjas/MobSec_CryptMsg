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
import android.util.Log;
import android.widget.Toast;

public class BinarySMSReceiver extends BroadcastReceiver {

	/**
	 * The Key
	 */
	private SecretKeySpec key;
	/**
	 * initialization vector for encryption
	 */
	private IvParameterSpec iv;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("beginOfBinReceive", "Incoming Binary SMS detected, starting onReceive()");
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		//Initialize the IV with zeros
		byte[] ivBytes = new byte[16];
		int a = 0;
		while(a<16){
			ivBytes[a] = 0;
			a++;
		}
		this.iv = new IvParameterSpec(ivBytes);
		String info = "Binary SMS received: ";
		if (null != bundle) {
			 Object[] pdusObj = (Object[]) bundle.get("pdus");
		        SmsMessage[] messages = new SmsMessage[pdusObj.length];

		        /* // getting SMS information from PDU
		        for (int i = 0; i < pdusObj.length; i++) {
		            messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
		        }

		       for (SmsMessage currentMessage : messages) {

		                String messageBody = currentMessage.getDisplayMessageBody();

		                byte[] messageByteArray = currentMessage.getPdu();

		                // skipping PDU header, keeping only message body
		                int x = 1 + messageByteArray[0] + 19 + 7;
		                byte[] encryptedText = new byte[messageByteArray.length-x];
		                for(int b=0; b<messageByteArray.length-x; b++)
		                	encryptedText[b]=messageByteArray[b+x];
		                byte[] decrypted=null;
						try {
							decrypted = decrypt(encryptedText);
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String realMessage = "";
						if(decrypted == null)
							realMessage = "Da ist was faul";
						else
							realMessage = new String(decrypted);
		                Toast.makeText(context, realMessage, Toast.LENGTH_LONG * 2).show();
		        }*/
			
			/*Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			
			byte[] data = null;

			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				info += msgs[i].getOriginatingAddress();
				info += "\n*****BINARY MESSAGE*****\n";

				data = msgs[i].getUserData();

				for (int index = 0; index < data.length; ++index) {
					info += Character.toString((char) data[index]);
				}
			}*/
			
		}
		Toast.makeText(context, info, Toast.LENGTH_LONG * 2).show();		
	}

	public byte[] encrypt(byte[] message) throws NoSuchAlgorithmException,
			NoSuchPaddingException, BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		try {
			cipher.init(Cipher.ENCRYPT_MODE, getKey(), this.iv);
		} catch (InvalidAlgorithmParameterException e) {
			// can't occur here since IV has fixed value
			e.printStackTrace();
		}
		return cipher.doFinal(message);
	}

	public byte[] decrypt(byte[] message) throws NoSuchAlgorithmException,
			NoSuchPaddingException, BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		try {
			cipher.init(Cipher.DECRYPT_MODE, getKey(), this.iv);
		} catch (InvalidAlgorithmParameterException e) {
			// can't occur here since IV has fixed value
			e.printStackTrace();
		}
		return cipher.doFinal(message);
	}

	/**
	 * Returns the encryption key.
	 * 
	 * @return The encryption key.
	 */
	public SecretKeySpec getKey() {
		return this.key;
	}

}
