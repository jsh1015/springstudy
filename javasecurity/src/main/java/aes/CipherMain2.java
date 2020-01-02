package aes;
/*
	키를 설정해서 AES 암호화 하기 : 키가 똑같기 때문에 암호문이 같음
 */
public class CipherMain2{
	public static void main(String[] args) throws Exception{
		String plain1 = "안녕하세요. 홍길동 입니다.";
		String key = "abc1234567";
		String cipher1 = CipherUtil.encrypt(plain1,key);
		System.out.println("암호문:" + cipher1);
		String plain2 = CipherUtil.decrypt(cipher1,key);
		System.out.println("복호문:" + plain2);
	}
}