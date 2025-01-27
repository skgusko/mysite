package mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import mysite.security.Auth;
import mysite.security.AuthUser;
import mysite.service.UserService;
import mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) { 
		this.userService = userService;
	}
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join(@ModelAttribute UserVo userVo) {
		return "/user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute @Valid UserVo userVo, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAllAttributes(result.getModel()); //다 풀어서 보냄. map.xx 이 아니라 xx로 바로 접근 가능
			
			return "/user/join";
		}
//		System.out.println(userVo); //여기엔 id가 없음
		userService.join(userVo);
//		System.out.println(userVo); //여기에 id가 있게 mybatis에서 설정 
		
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping("/joinsuccess")
	public String joinSuccess() {
		return "/user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "/user/login";
	}
	
	// LoginInterceptor, LogoutInterceptor로 대체
//	@RequestMapping(value="/login", method=RequestMethod.POST)
//	public String login(HttpSession session, UserVo userVo, Model model) {
//		UserVo authUser = userService.getUser(userVo.getEmail(), userVo.getPassword()); 
//		if (authUser == null) {
//			model.addAttribute("email", userVo.getEmail());
//			model.addAttribute("result", "fail");
//			return "user/login";
//		}
//		
//		// login 처리
//		session.setAttribute("authUser", authUser);
//		
//		return "redirect:/";
//	}
	
//	@RequestMapping("/logout")
//	public String logout(HttpSession session) {
//		session.removeAttribute("authUser");
//		session.invalidate();
//		
//		return "redirect:/";
//	}
	
	@Auth
	@RequestMapping(value="/update", method=RequestMethod.GET)
	public String update(@AuthUser UserVo authUser, Model model) {
		UserVo userVo = userService.getUser(authUser.getId());
		
		model.addAttribute("vo", userVo);
		return "user/update";
	}
	
	@Auth // 이 url로 들어올 땐 인증 받아야 해
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(@AuthUser UserVo authUser, UserVo userVo) {
		userVo.setId(authUser.getId());
		userService.update(userVo);
		
		authUser.setName(userVo.getName());
		return "redirect:/user/update";
	}
	
	@RequestMapping("/auth")
	public void auth() {
		
	}
	
	@RequestMapping("/logout")
	public void logout() {
		
	}
}
