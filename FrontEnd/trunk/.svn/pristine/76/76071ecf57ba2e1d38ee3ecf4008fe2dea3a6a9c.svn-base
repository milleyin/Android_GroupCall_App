package com.afmobi.palmchat.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Des {

	public static String decrypt(String encryptedText, String key) {
		byte[] results = {};
		try {
			byte[] bysDecoded = Base64Coder.decode(encryptedText);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec objDesKeySpec = new DESKeySpec(key.getBytes("ASCII"));
			SecretKeyFactory objKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey objSecretKey = objKeyFactory.generateSecret(objDesKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, objSecretKey);
			
			results = cipher.doFinal(bysDecoded);
			return new String(results, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String encrypt(String clearText, String key) {
		byte[] results = {};
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			DESKeySpec objDesKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory objKeyFactory = SecretKeyFactory
					.getInstance("DES");
			SecretKey objSecretKey = objKeyFactory
					.generateSecret(objDesKeySpec);

			cipher.init(Cipher.ENCRYPT_MODE, objSecretKey);
			results = cipher.doFinal((clearText).getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		char [] r = Base64Coder.encode(results);
		return new String(r);
	}
}
