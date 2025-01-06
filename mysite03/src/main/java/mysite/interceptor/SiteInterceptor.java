package mysite.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mysite.service.SiteService;

public class SiteInterceptor implements HandlerInterceptor {

	private LocaleResolver localeResolver;
	private SiteService siteService;
	
	public SiteInterceptor(LocaleResolver localeResolver, SiteService siteService) {
		this.localeResolver = localeResolver;
		this.siteService = siteService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ServletContext sc = request.getServletContext();
		
		// locale
		String lang = localeResolver.resolveLocale(request).getLanguage();
		request.setAttribute("lang", lang);
		
		//ServletContext's Attribute Map에 title 저장  
		if (sc.getAttribute("title") == null) {
			sc.setAttribute("title", siteService.getSite().getTitle());
		}
		
		return true;
	}
}