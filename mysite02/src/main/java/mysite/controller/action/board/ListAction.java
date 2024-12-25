package mysite.controller.action.board;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.controller.ActionServlet.Action;
import mysite.dao.BoardDao;
import mysite.vo.BoardVo;

public class ListAction implements Action {
	
	private static final int COUNT_PER_PAGE = 5;  // 한 페이지당 게시글 수
	private static final int PAGE_GROUP_COUNT = 5; // 한 번에 보여줄 페이지 수 (1,2,3,4,5)

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 현재 페이지 설정
		int currentPage = 1;
		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		BoardDao dao = new BoardDao();
		
		// 게시글 총 개수  
		int boardTotalCount = dao.getBoardTotalCount();
		
		// 총 페이지 수 계산
		int pageCount = (int) Math.ceil((double) boardTotalCount / COUNT_PER_PAGE);

		// 시작 페이지와 끝 페이지 계산
		int beginPage = ((currentPage - 1) / PAGE_GROUP_COUNT) * PAGE_GROUP_COUNT + 1;
		int endPage = beginPage + PAGE_GROUP_COUNT - 1;

		// 이전/다음 페이지 버튼 처리
		int prevPage = (beginPage > 1) ? beginPage - 1 : 0;
		int nextPage = (endPage < pageCount) ? endPage + 1 : 0;
		
//		int lastPage = ((currentPage - 1) / PAGE_GROUP_COUNT) * PAGE_GROUP_COUNT + 1 + PAGE_GROUP_COUNT - 1;
//		
//		int nextPage = (endPage != lastPage) ? endPage + 1 : 0;

		// 가져올 게시글의 시작 인덱스 
		int startIndex = (currentPage - 1) * COUNT_PER_PAGE;
		
		// 게시글 가져오기
		List<BoardVo> list = dao.findBoard(startIndex, COUNT_PER_PAGE);
		
		// JSP로 전달
		request.setAttribute("list", list);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("beginPage", beginPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("prevPage", prevPage);
		request.setAttribute("nextPage", nextPage);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		rd.forward(request, response);
	}
	/*
	private static final int COUNT_PER_PAGE = 5;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 현재 페이지 설정
		int currentPage = 1;
		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}
		
		BoardDao dao = new BoardDao();
		
		// 게시글 총 개수  
		int boardTotalCount = dao.getBoardTotalCount();
		
		// 페이지 개수 
		int pageCount = (int) Math.ceil(boardTotalCount / COUNT_PER_PAGE);
		
		// 가져올 게시글의 시작 인덱스 
		int startIndex = (currentPage-1) * COUNT_PER_PAGE;
		
		List<BoardVo> list = dao.findBoard(startIndex, COUNT_PER_PAGE);
		
		
//		List<BoardVo> list = new BoardDao().findAll();
		request.setAttribute("list", list);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("pageCount", pageCount);
		
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		rd.forward(request, response);
	}
	*/
}
