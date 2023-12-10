package com.poly.elnr.security;

import java.util.Collection;

import com.poly.elnr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.poly.elnr.service.serviceImpl.SessionService;
//import com.poly.elnr.service.UserServiceSecurity;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
    SessionService session;

	@Autowired
	UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		 UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		userService.findByUserNameLogin(userDetails.getUsername());


		boolean adminOrUser = false;
		adminOrUser = userDetails.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
						|| authority.getAuthority().equals("ROLE_STAFF"));

		 session.set("role",adminOrUser);

		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				response.sendRedirect("/user/index");
				return;
			} else if (authority.getAuthority().equals("ROLE_USER")) {

				response.sendRedirect("/user/index");
				return;
			}
		}
		response.sendRedirect("/user/index");
	}
}
