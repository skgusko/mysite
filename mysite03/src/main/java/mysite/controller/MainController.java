package mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import mysite.service.SiteService;
import mysite.vo.SiteVo;

@Controller
public class MainController {
	
	private final SiteService siteService;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	public MainController(SiteService siteService) {
		this.siteService = siteService;
	}
	
	@RequestMapping({"/", "/main"})
	public String index(Model model, HttpServletRequest request) {
		String lang = localeResolver.resolveLocale(request).getLanguage();
		SiteVo siteVo = siteService.getSite();
		
		model.addAttribute("lang", lang);
		model.addAttribute("siteVo", siteVo);
		
		return "main/index";
	}
}
