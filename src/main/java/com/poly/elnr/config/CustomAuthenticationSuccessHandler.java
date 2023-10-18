package com.poly.elnr.config;

import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.ui.Model;

import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.SessionService;
//import com.poly.elnr.service.UserServiceSecurity;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
    SessionService session;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
        // Kiểm tra trạng thái "active" của tài khoản
//        if (!authentication. .getActive()) {
//            // Nếu tài khoản không active, từ chối đăng nhập
//            response.sendRedirect("/signin?error=Tài khoản bạn đã bị khoá");
//            return;
//        }
		
		 UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	        // Lưu thông tin người dùng vào session 	
	        session.set("user", userDetails);
	        System.out.println("User logged in: " + userDetails.getUsername());
        // Lấy thông tin người dùng từ authentication
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		
		
		  System.out.println("Authorities for the authenticated user:");
	        for (GrantedAuthority authority : authorities) {
	            System.out.println(authority.getAuthority());
	        }
	        
	        
		
		// Tiến hành phân quyền
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ADMIN")) {
				// Nếu có vai trò "ADMIN", chuyển hướng đến "/admin/home"
				response.sendRedirect("/admin/chao");
				return;
			} else if (authority.getAuthority().equals("USER")) {
				// Nếu có vai trò "MANAGER", chuyển hướng đến "/manager/home"
				response.sendRedirect("/cline/chao");
				return;
			} else if (authority.getAuthority().equals("SELLER")) {
				// Nếu có vai trò "SELLER", chuyển hướng đến "/seller/home"
				response.sendRedirect("/seller/home");
				return;
			}
		}
//		String url = session.get("url");
//		if(url != null) {
//			response.sendRedirect(url);
//			return;
//		}
		// Mặc định chuyển hướng đến "/home" cho các vai trò khác
		response.sendRedirect("/user/index");
	}
	
	
}
