package util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class CipherUtil {
	private final static byte[] iv = new byte[] { 
			(byte)0x8E,0x12,0x39,(byte)0x9,
			      0x07,0x72,0x6F,(byte)0x5A,
		    (byte)0x8E,0x12,0x39,(byte)0x9,
			      0x07,0x72,0x6F,(byte)0x5A};
	static Cipher cipher;  //암호처리 객체
	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 알고리즘/블럭암호모드/패딩방법
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String makehash(String plain,String algo) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algo);
		byte[] plainByte = plain.getBytes();
		byte[] hashByte = md.digest(plainByte);
		return byteToHex(hashByte);
	}
	private String byteToHex(byte[] hashByte) {
		if(hashByte == null) return null;
		String str = "";
		for(byte b : hashByte) str += String.format("%02X", b);
		return str;
	}
	private static byte[] hexToByte(String str) {
		if (str == null || str.length() < 2)	return null; 
		int len = str.length() / 2;
		byte[] buf = new byte[len];
		for (int i = 0; i < len; i++) {
			buf[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}
		return buf;
	}
	private byte[] makeKey(String key) {
		int len = key.length();
		char ch = 'A';
		for (int i=len;i<16;i++) {
			key += ch++;
		}
		return key.substring(0,16).getBytes();
	}
	public String decrypt(String cipherMsg, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(makeKey(key),"AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE,genKey,paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	public String encrypt(String plain, String key) {
		byte[] cipherMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(makeKey(key), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, genKey, paramSpec);
			cipherMsg = cipher.doFinal(plain.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg);
	}
}
