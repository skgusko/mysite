package mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import mysite.repository.BoardRepository;
import mysite.vo.BoardVo;

@Service
public class BoardService {
	
	private BoardRepository boardRepository;
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	public void addContents(BoardVo vo) {
		if (vo.getgNo() != 0) { //답글 
			boardRepository.updateOrderNo(vo.getgNo(), vo.getoNo() + 1);
			
			vo.setoNo(vo.getoNo() + 1);
			vo.setDepth(vo.getDepth() + 1);
		}
		
		boardRepository.insert(vo);
	}
	
	public BoardVo getContents(Long id) {
		boardRepository.updateViews(id);
		
		return boardRepository.findById(id);
	}
	
	public BoardVo getContents(Long id, Long userId) { 
		return boardRepository.findByIdAndUserId(id, userId);
	}
	
	public void updateContents(BoardVo vo) {
		boardRepository.modify(vo);
	}
	
	public void deleteContents(Long id, Long userId) {
		BoardVo vo = boardRepository.findById(id);
		
		// 동일 유저 확인 
		if (userId.equals(vo.getUserId())) {
			boardRepository.deleteById(id);
		}
	}
	
	public Map<String, Object> getContentsList(int currentPage, String keyword) {
		Map<String, Object> map = new HashMap<>();

		//view의 pagination을 위한 데이터 값 계산 (맵으로 한 번에 보냄)
		final int COUNT_PER_PAGE = 5;  // 한 페이지당 게시글 수
		final int PAGE_GROUP_COUNT = 5; // 한 번에 보여줄 페이지 수 (1,2,3,4,5)
		
		// 게시글 총 개수  
		int boardTotalCount = boardRepository.totalCount(keyword);
		
		// 총 페이지 수 계산
		int pageCount = (int) Math.ceil((double) boardTotalCount / COUNT_PER_PAGE);

		// 시작 페이지와 끝 페이지 계산
		int beginPage = ((currentPage - 1) / PAGE_GROUP_COUNT) * PAGE_GROUP_COUNT + 1;
		int endPage = beginPage + PAGE_GROUP_COUNT - 1;

		// 이전/다음 페이지 버튼 처리
		int prevPage = (beginPage > 1) ? beginPage - 1 : 0;
		int nextPage = (endPage < pageCount) ? endPage + 1 : 0;

		// 가져올 게시글의 시작 인덱스 
		int startIndex = (currentPage - 1) * COUNT_PER_PAGE;
		
		// 게시글 가져오기
		List<BoardVo> list = boardRepository.findAllByPageAndKeword(startIndex, COUNT_PER_PAGE, keyword);
		
		map.put("list", list);
		map.put("currentPage", currentPage);
		map.put("pageCount", pageCount);
		map.put("beginPage", beginPage);
		map.put("endPage", endPage);
		map.put("prevPage", prevPage);
		map.put("nextPage", nextPage);
		map.put("keyword", keyword);
		
		return map;
	}
}
