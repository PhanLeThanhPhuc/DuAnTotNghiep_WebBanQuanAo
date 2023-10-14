package com.poly.elnr.interceptor;
import com.poly.elnr.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.poly.elnr.service.CategoryDetailService;
import com.poly.elnr.service.ColorService;
import com.poly.elnr.service.SizeService;
import com.poly.elnr.service.serviceImpl.ColorServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GlobalInterceptor implements HandlerInterceptor {

    private final SizeService sizeService;
    private final CategoryService categoryService;
    private final ColorService colorService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        
        if (requestURI.startsWith("/user/product")) {
        	 request.setAttribute("sizeMenu", sizeService.findAllSizeStatusTrue());
        	 request.setAttribute("colorMenu", colorService.findAllColorEight());
        	 request.setAttribute("colorPopover", colorService.findAllColor());
        }
        
        if (request.getHeader("X-Requested-With") == null) {
            request.setAttribute("category", categoryService.findALlCategory());
        }
        
    }
}

