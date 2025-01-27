package mysite.service;

import org.springframework.stereotype.Service;

import mysite.repository.SiteRepository;
import mysite.vo.SiteVo;

@Service
public class SiteService {
	
	private final SiteRepository siteRepository;
	public SiteService(SiteRepository siteRepository) {
		this.siteRepository = siteRepository;
	}

	//repository 있어야 대
	public SiteVo getSite() {
		return siteRepository.findSite();
	}
	
	public int updateSite(SiteVo siteVo) {
		//controller 파일업로드 서비스이용해서 url 받아서 얘를 불러야대 
		//이미지는 수정하지 않는 경우도 있기 때문에 multipart null인 경우 (isempty)도 고려해야대
		
		return siteRepository.updateSite(siteVo);
	}
}
