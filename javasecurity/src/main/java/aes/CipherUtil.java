package aes;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
	private static byte[] randomKey;
	private final static byte[] iv = new byte[] {
			(byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
				   0x07, 0x72, 0x6F, (byte) 0x5A,
  		    (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
				   0x07, 0x72, 0x6F, (byte) 0x5A };
	static Cipher cipher;
	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}									//AES
	public static byte[] getRandomKey(String algo) throws NoSuchAlgorithmException{
		//keyGen : AES용 키를 생성할 수 있는 객체
		KeyGenerator keyGen = KeyGenerator.getInstance(algo);
		//AES 알고리즘 : 128bit~192bit(가변적인크기) 키로 설정이 가능
		keyGen.init(128); //128비트짜리로 키 생성
		SecretKey key = keyGen.generateKey();
		return key.getEncoded();
	}
	//plain : 안녕하세요 홍길동입니다.
	public static String encrypt(String plain) {
		byte[] cipherMsg = new byte[1024];
		try {
			//암호화에 필요한 키를 생성
			randomKey = getRandomKey("AES");
			//key : 암호화에 필요한 키 객체
			Key key = new SecretKeySpec(randomKey,"AES");
			//초기화 벡터 객체 생성
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv); //이니셜벡터
			//Cipher.ENCRYPT_MODE : 암호화
			cipher.init(Cipher.ENCRYPT_MODE, key,paramSpec);
			//암호화 실행
			cipherMsg = cipher.doFinal(plain.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg).trim();
	}
	
	private static String byteToHex(byte[] cipherMsg) {
		if(cipherMsg == null) {
			return null;
		}
		String str = "";
		for(byte b:cipherMsg) {
			str += String.format("%02X", b);
		}
		return str;
	}
	public static String decrypt(String cipherMsg) {
		byte[] plainMsg = new byte[1024];
		try {
			Key key = new SecretKeySpec(randomKey, "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.DECRYPT_MODE : 복호화 모드
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();//byte형배열 을 String으로 바꿔 리턴
	}
	
	private static byte[] hexToByte(String str) {
		if(str == null) return null;
		if(str.length() < 2)
			return null;
		int len = str.length()/2;
		byte[] buf = new byte[len];
		for(int i=0;i<len;i++) {
			buf[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
		}
		return buf;
	}
	
	
	public static String encrypt(String plain, String key) {
		byte[] cipherMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(makeKey(key),"AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, genKey, paramSpec); //암호화설정
			cipherMsg = cipher.doFinal(plain.getBytes()); //설정된 대로
		}catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg); //byte형을 문자열로 변환한후 전달
	}
	
	//AES용 키의 크기 : 128비트 => 16바이트
	//키를 어떻게 만들지 설정해주고 16바이트 짜리로 만들게 함
	private static byte[] makeKey(String key) {
		int len = key.length();
		char ch = 'A';//B
		//key = abc1234567+ABC
		//i=10 : A
		//i=11 : B
		//i=12 : C
		for(int i=len;i<16;i++) key += ch++; //16바이트짜리 key로 만듬
		return key.substring(0,16).getBytes();
	}
	
	public static String decrypt(String cipher1, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genKey = new SecretKeySpec(makeKey(key),"AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, genKey, paramSpec); 
			plainMsg = cipher.doFinal(hexToByte(cipher1.trim())); //hexToByte : 문자열->byte형 배열
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	//key를 만들어줌
	public static String makehash(String msg) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] plain = msg.getBytes();
		byte[] hash = md.digest(plain);
		return byteToHex(hash);
	}
}
