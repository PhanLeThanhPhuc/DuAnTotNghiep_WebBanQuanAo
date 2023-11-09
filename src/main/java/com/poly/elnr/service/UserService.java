package com.poly.elnr.service;


import java.io.IOException;
import java.util.List;

import com.poly.elnr.dto.ChangePassword;
import com.poly.elnr.utils.CustomOAuth2User;


import org.springframework.security.core.userdetails.UserDetails;
import com.poly.elnr.entity.Users;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {

	public List<Users> findAll();

	Users create(Users user);

	public Users update(Users user);

	public void delete(Users id);

	UserDetails oauth2(CustomOAuth2User oAuth2User);

	Users findByEmail(String email);

	Users findByUserNameLogin(String username);

	public Users findById(Integer id) ;
	public List<Users> findAllUserByIdRole() ;

	void changePassword(String oldPassword, String username);

	String saveImageUser(MultipartFile multipartFile, String username) throws IOException;

	Users findByUserNamePhoneAndEmail(String username);

	int idUser(String username);
}
