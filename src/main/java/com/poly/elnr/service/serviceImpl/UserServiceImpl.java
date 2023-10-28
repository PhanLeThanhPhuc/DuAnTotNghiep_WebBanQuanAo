package com.poly.elnr.service.serviceImpl;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;

import com.poly.elnr.security.CustomUserDetails;
import com.poly.elnr.utils.CustomOAuth2User;
import com.poly.elnr.utils.RegexUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import com.poly.elnr.entity.Authority;
import com.poly.elnr.entity.Role;
import com.poly.elnr.entity.Users;
import com.poly.elnr.repository.AuthorityRepository;
import com.poly.elnr.repository.UserRepository;
import com.poly.elnr.service.UserService;
import com.poly.elnr.utils.RamDomNameUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	SessionService session;

	@Override
	public UserDetails oauth2(CustomOAuth2User oAuth2User) {
		
		String name = oAuth2User.getName();
		String email = oAuth2User.getEmail();
		String picture = oAuth2User.getPicture();
		
		Users user  = userRepository.findEmail(email);
		UserDetails userDetails;

		if(user == null) {
			user = new Users();
			user.setFullName(name);
			user.setEmail(email);
			user.setImage(picture);
			user.setPassword(passwordEncoder.encode(RamDomNameUtils.generateRandomPassword()));
			user.setDate_insert(new Date());
			user.setDate_update(new Date());
			userRepository.save(user);
			
			//set quyền
			user = new Users();
			user = userRepository.findEmail(email);
			Role roleId = new Role();
			roleId.setId("ROLE_USER");
			Users userid = new Users();
			userid.setId(user.getId());
			Authority authority = new Authority();
			authority.setUser(userid);
			authority.setRole(roleId);
			authorityRepository.save(authority);

			userDetails = User.withUsername(user.getEmail()).password(passwordEncoder.encode(RamDomNameUtils.generateRandomPassword()))
											.roles("USER").build();
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			session.set("user", user);
		}else {
			user = userRepository.findEmail(email);
			userDetails = new CustomUserDetails(user);
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			session.set("user", user);
		}
		return userDetails;
	}

	@Override
	public Users findByEmail(String email) {
		return userRepository.findEmail(email);
	}

	@Override
	public void findByUserNameLogin(String username) {

		UserDetails userDetails;
		Users user;

		if(RegexUtils.isPhoneNumber(username)) {
			user = userRepository.findByPhone(username);
		}else{
			user = userRepository.findEmail(username);
		}
		userDetails = new CustomUserDetails(user);
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		session.set("user", user);
	}

	@Override
	public List<Users> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public Users create(Users user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public Users update(Users user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public void delete(Users id) {
		// TODO Auto-generated method stub
		userRepository.delete(id);
	}

	@Override
	public Users findById(Integer id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id).get();
	}

	@Override
	public List<Users> findAllUserByIdRole() {
		// TODO Auto-generated method stub
		return userRepository.findAllUserByIdRole();
	}


}
