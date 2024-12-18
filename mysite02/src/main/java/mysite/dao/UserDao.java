package mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import mysite.vo.UserVo;

public class UserDao {

	public int insert(UserVo vo) {
		int count = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("insert into user values(null, ?, ?, ?, ?, now())");
		) {
			pstmt.setString(1, vo.getName()); 
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return count;
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

}
