package se.kth.mobsec.cryptmsg;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class AesCtr {
	
	/**
	 * Encrypts a message using AES with a given key (as a byte array).
	 * 
	 */
	public static byte[] encrypt(byte[] plaintext, byte[] keyBytes, byte[] ivBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException{
		
		keyBytes = padKey(keyBytes);
		
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//	    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	    Cipher cipher = getCipher();

	    Log.i("AesCtr.encrrypt()", "die nachricht: " + new String(plaintext));
	    // encryption pass
	    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	    
	    return cipher.doFinal(plaintext);
	}
	
	private static byte[] padKey(byte[] key) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit
		return key;
	}

	private static Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
		return Cipher.getInstance("AES");
	}

	/**
	 * Decrypts a message using an AESKey given as a byte array.
	 * 
	 */
	public static byte[] decrypt(byte[] ciphertext, byte[] keyBytes, byte[] ivBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException{
		
		keyBytes = padKey(keyBytes);
		
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		//SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	    Cipher cipher = getCipher();
	    
	    // decryption pass
	    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
	    return cipher.doFinal(ciphertext);
	}
	
	
	public static byte[] getAESKey(byte[] keyRaw) {
		byte[] key = new byte[24];
		byte[] keyTmp = keyRaw;
		if (keyTmp.length >= key.length)
			for (int i = 0; i < key.length; i++)
				key[i] = keyTmp[i];
		else {
			for (int i = 0; i < keyTmp.length; i++)
				key[i] = keyTmp[i];
			for (int i = keyTmp.length; i < key.length; i++)
				key[i] = 'a';
		}
		return key;
	}

}

