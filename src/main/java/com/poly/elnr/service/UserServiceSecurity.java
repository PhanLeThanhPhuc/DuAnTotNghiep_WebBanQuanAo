package com.poly.elnr.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.poly.elnr.repository.UserRepository;

@Service
public record UserServiceSecurity(UserRepository repository,
        PasswordEncoder passwordEncoder) {
//	public String addUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        repository.save(user);
//        return "Thêm user thành công!";
//    }
    
	public void loginFromOAuth2(OAuth2AuthenticationToken oauth2){
		// String fullname = oauth2.getPrincipal().getAttribute("name");
		String email = oauth2.getPrincipal().getAttribute("email");
		String password = Long.toHexString(System.currentTimeMillis());
		
		UserDetails user = User.withUsername(email)
				.password(passwordEncoder.encode(password)).roles("ROLE_USER").build();
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
