package mysite.service;

import java.util.List;

import org.springframework.stereotype.Service;

import mysite.repository.GuestbookRepository;
import mysite.vo.GuestbookVo;

@Service
public class GuestbookService {
	
	private GuestbookRepository guestbookRepository;
	
	private GuestbookService(GuestbookRepository guestbookRepository) {
		this.guestbookRepository = guestbookRepository;
	}
	
	public List<GuestbookVo> getContentsList() {
		List<GuestbookVo> list = guestbookRepository.findAll();
		return list;
	}
	
	public void deleteContents(Long id, String password) {
		guestbookRepository.deleteByIdAndPassword(id, password);
	}
	
	public void addContents(GuestbookVo vo) {
		guestbookRepository.insert(vo);
	}
}
