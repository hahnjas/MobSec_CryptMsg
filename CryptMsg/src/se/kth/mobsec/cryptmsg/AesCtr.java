package se.kth.mobsec.cryptmsg;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class AesCtr {
	
	/**
	 * Encrypts a message using AES with a given key (as a byte array).
	 * @param plaintext
	 * @param keyBytes key as byte array
	 * @param ivBytes initialization vector as byte array
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encrypt(byte[] plaintext, byte[] keyBytes, byte[] ivBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException{
		
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//	    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


	    Log.i("AesCtr.encrrypt()", "die nachricht: " + new String(plaintext));
	    // encryption pass
	    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	    return cipher.doFinal(plaintext);
//	    ByteArrayInputStream bIn = new ByteArrayInputStream(plaintext);
//	    CipherInputStream cIn = new CipherInputStream(bIn, cipher);
//	    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//	    int ch;
//	    while ((ch = cIn.read()) >= 0) bOut.write(ch);
//	    bOut.close();
//	    return bOut.toByteArray();
	}
	
	/**
	 * Decrypts a message using an AESKey given as a byte array.
	 * @param plaintext
	 * @param keyBytes key as byte array
	 * @param ivBytes initialization vector as byte array
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] decrypt(byte[] ciphertext, byte[] keyBytes, byte[] ivBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException{
		
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		//SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
	    
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

