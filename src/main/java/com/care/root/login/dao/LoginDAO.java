package com.care.root.login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	public LoginDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이브 로드 성공");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "dbwls","dbwls9874");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//사용자한테 넘어오는 값 비교하기
	public int loginChk(String userId, String userPwd) {
		String sql = "select * from jsp_member where id='"+userId+"'";
		int result = 0;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {// 해당 아이디가 존재한다면
				if(userPwd.equals(rs.getString("pwd"))) {// 비밀번호 일치
					result = 0;
				}else {//비밀번호 불일치
					result = 1;
				}
			}else {//해당 아이디가 없을 경우 실행
				result = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
