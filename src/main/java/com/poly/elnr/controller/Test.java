package com.poly.elnr.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.elnr.service.UserService;

@RestController
public class Test {
	
	@Autowired
	UserService userService;
	
//	 @GetMapping("/user/controller")
//	    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
//	        return oAuth2User.getAttributes();
//	    }
}
