package com.poly.elnr.service;

import com.poly.elnr.entity.Users;
import com.poly.elnr.utils.CustomOAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {

	UserDetails oauth2(CustomOAuth2User oAuth2User);

	Users findByEmail(String email);

	void findByUserNameLogin(String username);

}
