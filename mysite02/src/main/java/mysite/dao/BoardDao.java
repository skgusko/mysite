package mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mysite.vo.BoardVo;
import mysite.vo.UserVo;

public class BoardDao {

	public List<BoardVo> findAll() {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select b.id, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), b.depth" +
						"  from board b join user u" +
						"    on b.user_id=u.id" +
						" order by g_no desc, o_no asc");
				ResultSet rs = pstmt.executeQuery();
		)
		{
			while(rs.next()) {
				Long id = rs.getLong(1);
				String title = rs.getString(2);
				String userName = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int depth = rs.getInt(6);
				
				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setUserName(userName);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return result;
	}
	
	private Connection getConnection() throws SQLException{
		Connection conn = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.56.5:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
			
		} catch (ClassNotFoundException e) { 
			System.out.println("드라이버 로딩 실패: " + e);
		} 
		return conn;
	}

	public BoardVo findById(Long id) {
		BoardVo boardVo = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select title, contents, g_no, o_no, depth, user_id from board where id=?");
		) {
			pstmt.setLong(1, id); 
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String title = rs.getString(1);
				String contents = rs.getString(2);
				int gNo = rs.getInt(3);
				int oNo = rs.getInt(4);
				int depth = rs.getInt(5);
				Long userId = rs.getLong(6); //작성자일 경우에만 수정 가능하도록 
				
				boardVo = new BoardVo();
				boardVo.setId(id);
				boardVo.setTitle(title);
				boardVo.setContents(contents);
				boardVo.setgNo(gNo);
				boardVo.setoNo(oNo);
				boardVo.setDepth(depth);
				boardVo.setUserId(userId);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return boardVo;
	}

	public int write(BoardVo vo) {
		int count = 0;
		
		try (
				Connection conn = getConnection();	
				PreparedStatement pstmt = conn.prepareStatement("insert into board values(null, ?, ?, ?, now(), ?, ?, ?, ?)");
		) {
			pstmt.setString(1, vo.getTitle()); 
			pstmt.setString(2, vo.getContents());
			pstmt.setString(3, String.valueOf(vo.getHit())); //hit
			pstmt.setString(4, String.valueOf(vo.getgNo())); //g_no : max
			pstmt.setString(5, String.valueOf(vo.getoNo())); //o_no
			pstmt.setString(6, String.valueOf(vo.getDepth())); //depth
			pstmt.setString(7, String.valueOf(vo.getUserId()));
			
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return count;
		
	}

	public int findgNo() {
		int maxgNo = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select max(g_no) from board");
				ResultSet rs = pstmt.executeQuery();
		) {
			if (rs.next()) {
				maxgNo = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return maxgNo + 1; //새로운 그룹 번호 
	}
}
