package com.poly.elnr.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.poly.elnr.service.serviceImpl.SessionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
	
	@Autowired 
	SessionService session;
	
	@Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        // Xóa thông tin người dùng khỏi session khi đăng xuất thành công
		session.remove("user");
        // Chuyển hướng sau khi đăng xuất thành công
        response.sendRedirect("/security/user");
    }
}
