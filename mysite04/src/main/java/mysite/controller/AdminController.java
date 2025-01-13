package mysite.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import mysite.security.Auth;
import mysite.service.FileUploadService;
import mysite.service.SiteService;
import mysite.vo.SiteVo;

@Auth(role="ADMIN") //이 Controller의 모든 handler
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final SiteService siteService;
	private final FileUploadService fileUploadService;
	private final ServletContext servletContext;
	private final ApplicationContext applicationContext;
	
	public AdminController(
			SiteService siteService, 
			FileUploadService fileUploadService,
			ServletContext servletContext,
			ApplicationContext applicationContext) {
		this.siteService = siteService;
		this.fileUploadService = fileUploadService;
		this.servletContext = servletContext;
		this.applicationContext = applicationContext;
	}
	
	@RequestMapping({"", "/main"})
	public String main(Model model) {
//		SiteVo siteVo = siteService.getSite();
//		model.addAttribute("siteVo", siteVo);
		
		return "admin/main";
	}
	
	@RequestMapping("/main/update")
	public String update(SiteVo siteVo, 
			@RequestParam("file") MultipartFile file,
			HttpSession session) {
		String newProfile = fileUploadService.restore(file); //파일 저장 + url 변경 
		
		if (newProfile != null) {
			siteVo.setProfile(newProfile);
		}
		
		siteService.updateSite(siteVo);
		
		// update servlet context bean
		servletContext.setAttribute("siteVo", siteVo);
		
		// update application context bean
		SiteVo site = applicationContext.getBean(SiteVo.class);
		BeanUtils.copyProperties(siteVo, site);
		
		return "redirect:/admin";
	}
	
	@RequestMapping("/guestbook")
	public String guestbook() {
		return "admin/guestbook";
	}
	
	@RequestMapping("/board")
	public String board() {
		return "admin/board";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "admin/user";
	}
}
