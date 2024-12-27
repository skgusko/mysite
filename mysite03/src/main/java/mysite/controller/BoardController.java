package mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import mysite.service.BoardService;
import mysite.vo.BoardVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	private BoardService boardService;
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@RequestMapping({"", "/"})
	public String main(@RequestParam(value="page", required=false) Integer currentPage, 
					   @RequestParam(value="kwd", required=false) String keyword) {
		if (currentPage == null) {
			currentPage = 1;
		}
		
		//boardService.getContentsList(currentPage, keyword);
		
		return "board/list";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write() {
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(HttpSession session, BoardVo vo) {
		if (session == null || session.getAttribute("authUser") == null) {
			return "user/login";
		}
		
		BoardVo authUser = (BoardVo)session.getAttribute("authUser");
		
		
		
		
		return "redirect:/board";
	}
}
