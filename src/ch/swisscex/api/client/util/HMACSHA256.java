package ch.swisscex.api.client.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HMACSHA256 {
	
	private static final Logger log = LoggerFactory.getLogger(HMACSHA256.class);
	
	public static final String ALGORITHM = "HmacSHA256";
	
	private final Mac sha256Mac;
	private final SecretKeySpec keySpec;
	
	public HMACSHA256(String secretKey) {
		if(null == secretKey) {
			throw new IllegalArgumentException("secretKey cannot be null");
		}
		sha256Mac = createMac();
		keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
	}
	
	public String hash(String data) {
		String result = null;
		if(null != data) {
			try {
				sha256Mac.init(keySpec);
				final byte[] macData = sha256Mac.doFinal(data.getBytes());
				result = Hex.encodeHexString(macData);
			} catch (InvalidKeyException e) {
				log.error(e.getMessage(), e);
			}
		}
		return result;
	}
	
	private Mac createMac() {
		Mac sha256Mac = null;
		try {
			sha256Mac = Mac.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		}
		return sha256Mac;
	}
}
