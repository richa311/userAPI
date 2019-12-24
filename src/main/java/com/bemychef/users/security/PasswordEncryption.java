package com.bemychef.users.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordEncryption {

	private static Logger logger = LoggerFactory.getLogger(PasswordEncryption.class);

	private PasswordEncryption() {
		// empty implementation
	}

	private static final String KEY = "bemyChefEncptKey";
	private static final String INIT_VECTOR = "encryptionIntVec";

	public static String encrypt(String value) {
		logger.debug("Encrypting password starts..");
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			logger.error("Got error while encoding the password :" + ex.toString());
		}
		logger.debug("Encrypting password starts..");
		return null;
	}

}
