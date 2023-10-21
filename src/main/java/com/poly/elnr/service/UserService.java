package com.poly.elnr.service;


import java.util.List;
import com.poly.elnr.utils.CustomOAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.poly.elnr.entity.Category;
import com.poly.elnr.entity.User;


public interface UserService {

	public List<User> findAll();

	User create(User user);

	public User update(User user);

	public void delete(User id);

	UserDetails oauth2(CustomOAuth2User oAuth2User);

	Users findByEmail(String email);

	void findByUserNameLogin(String username);


}
