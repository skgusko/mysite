package mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	
	@Autowired
	private DataSource dataSource;
	private SqlSession sqlSession;
	
	public BoardRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int totalCount(String keyword) {
		return sqlSession.selectOne("board.totalCount", keyword);
	}
	
	public List<BoardVo> findAllByPageAndKeword(int startIndex, int size, String keyword) {
		return sqlSession.selectList("board.findAllByPageAndKeword", 
							  Map.of("startIndex", startIndex, 
									 "size", size,
									 "keyword", keyword != null ? keyword : ""));
	}

	public int insert(BoardVo vo) {
		return sqlSession.insert("board.insert", vo);
	}

	public int deleteById(Long id) {
		return sqlSession.delete("board.deleteById", id);
	}

	public BoardVo findById(Long id) {
		return sqlSession.selectOne("board.findById", id);
	}

	public int updateViews(Long id) {
		return sqlSession.update("board.update", id);
	}
	
	public int modify(BoardVo vo) {
		return sqlSession.update("board.modify", vo);
	}

	public BoardVo findByIdAndUserId(Long id, Long userId) {
		return sqlSession.selectOne("board.findByIdAndUserId", Map.of("id", id, "userId", userId));
	}


	public int updateOrderNo(int gNo, int oNo) {
		return sqlSession.update("board.updateOrderNo", Map.of("gNo", gNo, "oNo", oNo));
		
//		int count = 0;
//		
//		try (
//				Connection conn = dataSource.getConnection();
//				PreparedStatement pstmt1 = conn.prepareStatement("update board set o_no=o_no+1 where g_no=? and o_no>=?+1");
//		) {
//				pstmt1.setInt(1, gNo);
//				pstmt1.setInt(2, oNo);
//				
//				count = pstmt1.executeUpdate();
//		} catch (SQLException e) {
//			System.out.println("error: " + e);
//		}
//		return count;
	}


}
