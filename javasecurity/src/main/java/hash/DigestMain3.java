package hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/*
	화면에서 아이디와 비밀번호를 입력 받아서
	해당 아이디가 userbackup 테이블에 없으면 "아이디 확인" 출력
	해당 아이디의 비밀번호를 비교해서 맞으면 "반갑습니다. 아이디님 " 출력
	해당 아이디의 비밀번호를 비교해서 틀리면 "비밀번호 확인" 출력
 */
public class DigestMain3{
	public static void main(String[] arg) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //readline을 사용할 수 있음
		//db설정
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/classdb","scott","1234");
		PreparedStatement pstmt = conn.prepareStatement("select password from userbackup where userid=?");
		System.out.print("ID : ");
		String id = br.readLine();
		System.out.print("PW : ");
		String pw = br.readLine();
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		if(rs.next()) { //id가 있는 경우
			String hashpass = "";
			byte[] plain = pw.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) {
				hashpass += String.format("%02X", b);
			}
			if(rs.getString("password").equals(hashpass)){
				System.out.println("반갑습니다. "+id+"님");
			}else {
				System.out.println("비밀번호 확인");
			}
		}else {
			System.out.println("아이디 확인");
		}
	}
}
