package mysite.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import mysite.service.BoardService;
import mysite.vo.BoardVo;
import mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	private BoardService boardService;
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@RequestMapping({"", "/", "/search"})
	public String main(@RequestParam(value="page", required=false, defaultValue="1") Integer currentPage, 
					   @RequestParam(value="kwd", required=false) String keyword,
					   Model model) {
		Map<String, Object> map = boardService.getContentsList(currentPage, keyword);
		model.addAttribute("map", map);
		
		return "board/list";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write(@RequestParam(value="id", required=false) Long id,
						Model model) {
		if (id != null) { //답글쓰기인 경우 
			BoardVo vo = boardService.getContents(id); //부모글
			model.addAttribute("vo", vo);
		}
		
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(HttpSession session, BoardVo vo) {
		if (session == null || session.getAttribute("authUser") == null) {
			return "user/login";
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		vo.setUserId(authUser.getId());
		
		boardService.addContents(vo);
		
		return "redirect:/board";
	}
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") Long id,
					   @RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
					   Model model) {
		
		BoardVo vo = boardService.getContents(id);
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("vo", vo);
		
		return "board/view";
	}
	
	@RequestMapping("/delete/{id}")
	public String delete(HttpSession session, 
			   			 @RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
						 @PathVariable("id") Long id,
						 BoardVo boardVo,
						 Model model) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		if (session == null || authUser == null) {
			return "user/login";
		}
		
		boardService.deleteContents(id, authUser.getId());
		
		
		return "redirect:/board?page=" + currentPage;
	}
	
	@RequestMapping(value="/modify/{id}", method=RequestMethod.GET)
	public String modify(HttpSession session, 
			   			 @RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
						 @PathVariable("id") Long id,
						 Model model) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		if (session == null || authUser == null) {
			return "user/login";
		}
		
		BoardVo boardVo = boardService.getContents(id);
		model.addAttribute("boardVo", boardVo);
		model.addAttribute("currentPage", currentPage);
		
		return "board/modify";
	}
	
	@RequestMapping(value="/modify/{id}", method=RequestMethod.POST)
	public String modify(HttpSession session, 
			   			 @RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
						 @PathVariable("id") Long id,
						 BoardVo boardVo) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		if (session == null || authUser == null) {
			return "user/login";
		}
		
		BoardVo vo = boardService.getContents(id, authUser.getId());
		
		vo.setTitle(boardVo.getTitle());
		vo.setContents(boardVo.getContents());
		
		boardService.updateContents(boardVo);
		
		return "redirect:/board/view/" + id + "?page=" + currentPage;
	}
}
