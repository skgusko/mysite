package mysite.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import mysite.vo.GuestbookVo;


@Repository
public class GuestbookRepository {
	
	private SqlSession sqlSession;
	
	public GuestbookRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public List<GuestbookVo> findAll() {
		return sqlSession.selectList("guestbook.findAll");
	}

	public int insert(GuestbookVo vo) {
		return sqlSession.insert("guestbook.insert", vo);
	}

	public int deleteByIdAndPassword(Long id, String password) {
		return sqlSession.delete("guestbook.deleteByIdAndPassword", Map.of("id", id, "password", password));
	}

//	public String findPasswordById(Long id) {
//		String result = "";
//		
//		try (
//				Connection conn = dataSource.getConnection();
//				PreparedStatement pstmt = conn.prepareStatement("select password from guestbook where id=?");
//				ResultSet rs = pstmt.executeQuery();
//		)
//		{
//			if(rs.next()) {
//				result = rs.getString(1);
//			}
//		} catch (SQLException e) {
//			System.out.println("error: " + e);
//		}
//		return result;
//	}
}
