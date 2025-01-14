package mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import mysite.vo.SiteVo;

@Repository
public class SiteRepository {

	private final SqlSession sqlSession;
	public SiteRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public SiteVo findSite() {
		return sqlSession.selectOne("site.findSite");
	}
	
	public int updateSite(SiteVo vo) {
		return sqlSession.update("site.update", vo);
	}
}
