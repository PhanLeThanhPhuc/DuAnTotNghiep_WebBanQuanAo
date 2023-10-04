
package com.poly.elnr.interceptor;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.poly.elnr.service.CategoryDetailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class GlobalInterceptor implements HandlerInterceptor {
	
	@Autowired
	CategoryDetailService categoryDetailService;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(request.getHeader("X-Requested-With") == null) {
//			request.setAttribute("cates", categoryService.findAll());
			request.setAttribute("category",categoryDetailService.findCategoryAndCategoryDetail());
		}
	}
}
