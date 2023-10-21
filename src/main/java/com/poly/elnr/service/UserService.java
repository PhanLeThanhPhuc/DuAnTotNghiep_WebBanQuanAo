package com.poly.elnr.service;


import java.util.List;

import com.poly.elnr.utils.CustomOAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.Users;


public interface UserService {

	public List<Users> findAll();

	Users create(Users user);

	public Users update(Users user);

	public void delete(Users id);

	UserDetails oauth2(CustomOAuth2User oAuth2User);

	Users findByEmail(String email);

	void findByUserNameLogin(String username);


}
