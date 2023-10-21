package com.poly.elnr.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {

	void oauth2(OAuth2User oAuth2User);
	
}
