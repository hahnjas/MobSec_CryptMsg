package se.kth.mobsec.cryptmsg;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import android.util.Base64;

/**
 * Handles client-side en- and decryption of the CaaS system. 
 * @author Jasper
 *
 */
public class CryptoClient {
	
	/**
	 * The secret used for creating the authentication hash.
	 */
	private static String secret = "geheim";

	/**
	 * Encrypt Data when starting encrypted communication.
	 * @param session
	 */
	public static String encryptOutgoingMessage(String secret, String message) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
	
		String iv = "112";

		byte[] key = secret.getBytes();
		
		byte[] messageBytes = message.getBytes();
		byte[] ivBytes = iv.getBytes();
		byte[] encryptedBytes = se.kth.mobsec.cryptmsg.AesCtr.encrypt(messageBytes, key, ivBytes);
		String encryptedMessage = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
		return encryptedMessage;
		
	}

	
	/**
	 * Decrypt incoming message after receiving it from CaaS server in order to get plain text message.
	 */
	public static String decryptIncomingMessage(String secret, String encryptedMessage) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException{
		
		String iv = "112";
		
		byte[] key = secret.getBytes();
		byte[] messageBytes = Base64.decode(encryptedMessage, Base64.DEFAULT);
		byte[] ivBytes = iv.getBytes();
		byte[] decryptedBytes = AesCtr.decrypt(messageBytes, key, ivBytes);
		String decryptedMessage = new String(decryptedBytes);
		return decryptedMessage;
		
	}
	

	/**
	 * Getetrs and setters for user's secret.
	 * @return
	 */
	public static String getSecret() {
		return secret;
	}

	public static void setSecret(String secret) {
		CryptoClient.secret = secret;
	}
	
	
	
}
