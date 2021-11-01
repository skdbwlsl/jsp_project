package com.care.root.board.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.care.root.board.dto.BoardDTO;
import com.care.root.paging.PageCount;

public class BoardDAO {
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	public BoardDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이브 로드 성공");
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "dbwls", "dbwls9874");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ArrayList<BoardDTO> list(int start, int end){
		//String sql = "select * from test_board";
		//String sql = "select * from test_board order by idgroup desc,step asc"; //desc :내림차순, asc:내림차순
		String sql= "select B.* from(select rownum rn, A.* from"
				+ "(select * from test_board order by idgroup desc)A)B where rn between ? and ?";
	
		ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, start);
			ps.setInt(2, end);
			rs = ps.executeQuery();
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				
				dto.setSavedate(rs.getTimestamp("savedate"));
				dto.setHit(rs.getInt("hit"));
				dto.setIdgroup(rs.getInt("idgroup"));
				dto.setStep(rs.getInt("step"));
				dto.setIndent(rs.getInt("indent"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public void writeSave(String name, String title, String content) {
		String sql = 
		"insert into test_board(id, name, title, content, idgroup, step, indent) "
		+ "values(test_board_seq.nextval,?,?,?, test_board_seq.currval,0,0)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, title);
			ps.setString(3, content);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void upHit(String num) {
		String sql = "update test_board set hit=hit+1 where id="+num;
		try {
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public BoardDTO contentView(String num,int flag) {
		if(flag==1)
		upHit(num);
		
		String sql = "select * from test_board where id="+num;
		BoardDTO dto = new BoardDTO();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				dto.setId(rs.getInt("id"));
				dto.setName(rs.getString("name"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				
				dto.setSavedate(rs.getTimestamp("savedate"));
				dto.setHit(rs.getInt("hit"));
				dto.setIdgroup(rs.getInt("idgroup"));
				dto.setStep(rs.getInt("step"));
				dto.setIndent(rs.getInt("indent"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	public void modify(String id, String name, String title, String content) {
		String sql = "update test_board set name=?, title=?, content=? where id=?";
		try {
			ps=con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, title);
			ps.setString(3, content);
			ps.setString(4, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int delete(String id) {
		String sql = "delete from test_board where id="+id;
		int result = 0;
		try {
			ps = con.prepareStatement(sql);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	private void replyShape(BoardDTO dto) {
		String sql="update test_board set step =step+1 where idgroup=? and step >?";
		try {
			ps=con.prepareStatement(sql);
			ps.setInt(1, dto.getIdgroup());
			ps.setInt(2, dto.getStep());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void reply(BoardDTO dto) {
		replyShape(dto);
		String sql="insert into test_board(id,name, title, content, idgroup, step, indent)"
				+ "values(test_board_seq.nextval,?,?,?,?,?,?)"; //? 6개는 id,name, title, content, idgroup, step, indent
		try {//db로 전송
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getName()); //순서지정
			ps.setString(2, dto.getTitle());
			ps.setString(3, dto.getContent());
			
			ps.setInt(4, dto.getIdgroup());
			ps.setInt(5, dto.getStep()+1); 
			ps.setInt(6, dto.getIndent()+1);
			
			ps.executeUpdate(); //이게 있어야 실행됨
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public int getTotalPage() {
		String sql = "select count(*) from test_board";
		int totPage = 0;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				totPage = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totPage;
	}
	public PageCount pagingNum(int start) {
		PageCount pc = new PageCount();
		if(start == 0) start = 1; //start = 1 : 페이지 시작번호
		
		int pageNum = 3;
		int totalPage = getTotalPage();
		
		int totEndPage = totalPage / pageNum;
		if(totalPage % pageNum != 0) {
			totEndPage = totEndPage + 1;
		}
		
		int endPage = start * pageNum;
		int startPage = endPage + 1 - pageNum;
		
		pc.setTotEndPage(totEndPage);
		pc.setStartPage(startPage);
		pc.setEndPage(endPage);
		
		return pc;
	}
}
