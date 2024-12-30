package mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	
	public List<BoardVo> findBoard(int startIndex, int countPerPage) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select b.id, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), b.depth, u.id" +
						"  from board b join user u" +
						"    on b.user_id=u.id" +
						" order by g_no desc, o_no asc" + 
						" limit ?, ?");
		)
		{
			pstmt.setInt(1, startIndex);
			pstmt.setInt(2, countPerPage);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long id = rs.getLong(1);
				String title = rs.getString(2);
				String userName = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int depth = rs.getInt(6);
				Long userId = rs.getLong(7);
				
				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setUserName(userName);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setUserId(userId);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return result;
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

	public BoardVo findByIdAndUserId(Long id, Long userId) {
		BoardVo boardVo = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select title, contents from board where id=? and user_id=?");
		) {
			pstmt.setLong(1, id);
			pstmt.setLong(2, userId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String title = rs.getString(1);
				String contents = rs.getString(2);
				
				boardVo = new BoardVo();
				boardVo.setId(id);
				boardVo.setTitle(title);
				boardVo.setContents(contents);
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
			pstmt.setInt(3, vo.getHit()); //hit
			pstmt.setInt(4, vo.getgNo()); //g_no : max
			pstmt.setInt(5, vo.getoNo()); //o_no
			pstmt.setInt(6, vo.getDepth()); //depth
			pstmt.setLong(7, vo.getUserId());
			
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return count;
		
	}

	public int getNextGroupNo() {
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

	public int modify(BoardVo vo) {
		int count = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("update board set title=?, contents=? where id=?");
		) {
				pstmt1.setString(1, vo.getTitle()); 
				pstmt1.setString(2, vo.getContents());
				pstmt1.setLong(3, vo.getId());
				
				count = pstmt1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return count;
	}

	/*
	public BoardVo findForReply(Long id) {
		BoardVo boardVo = null;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select g_no, o_no, depth, title, contents from board where id=?");
		) {
			pstmt.setLong(1, id); 
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int gNo = rs.getInt(1);
				int oNo = rs.getInt(2);
				int depth = rs.getInt(3);
				String title = rs.getString(4);
				String contents = rs.getString(5);
				
				boardVo = new BoardVo();
				boardVo.setgNo(gNo);
				boardVo.setoNo(oNo);
				boardVo.setDepth(depth);
				boardVo.setTitle(title);
				boardVo.setContents(contents);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return boardVo;
	}
	*/

	public int updateOrderNo(int gNo, int oNo) {
		int count = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("update board set o_no=o_no+1 where g_no=? and o_no>=?+1");
		) {
				pstmt1.setInt(1, gNo);
				pstmt1.setInt(2, oNo);
				
				count = pstmt1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return count;
	}

	public int deleteById(Long id) {
		int count = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from board where id=?");
		) {
			pstmt.setLong(1, id);
			
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		
		return count;
	}

	public int getBoardTotalCount() {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) from board");
		) {
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return result;
	}


	public int updateViews(Long id) {
		int count = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("update board set hit=hit+1 where id=?");
		) {
				pstmt1.setLong(1, id); 
				
				count = pstmt1.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return count;
	}

	public int getCountByKwd(String keyword) {
		int result = 0;
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select count(*) from board where title like ? or contents like ?");
		) {
			String searchKeyword = "%" + keyword + "%";
			
			pstmt.setString(1, searchKeyword); 
			pstmt.setString(2, searchKeyword); 
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return result;
	}


	public List<BoardVo> findBoardByKwd(String keyword, int startIndex, int countPerPage) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(
						"select b.id, b.title, u.name, b.hit, date_format(b.reg_date, '%Y-%m-%d %h:%i:%s'), b.depth, u.id" +
						"  from board b join user u" +
						"    on b.user_id=u.id" +
						" where title like ? or contents like ?" +
						" order by g_no desc, o_no asc" + 
						" limit ?, ?");
		)
		{
			String searchKeyword = "%" + keyword + "%";
			
			pstmt.setString(1, searchKeyword);
			pstmt.setString(2, searchKeyword);
			pstmt.setInt(3, startIndex);
			pstmt.setInt(4, countPerPage);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Long id = rs.getLong(1);
				String title = rs.getString(2);
				String userName = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int depth = rs.getInt(6);
				Long userId = rs.getLong(7);
				
				BoardVo vo = new BoardVo();
				vo.setId(id);
				vo.setTitle(title);
				vo.setUserName(userName);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setUserId(userId);
				
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
}
