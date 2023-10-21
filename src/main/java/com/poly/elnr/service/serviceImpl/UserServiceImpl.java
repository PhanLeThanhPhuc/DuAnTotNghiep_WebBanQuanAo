package com.poly.elnr.service.serviceImpl;

import java.util.Date;
import java.util.Random;

import com.poly.elnr.config.UserInfoUserDetails;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	SessionService session;

	@Override
	public void oauth2(OAuth2User oAuth2User) {
		
		String name = oAuth2User.getAttribute("name");
		String email = oAuth2User.getAttribute("email");
		String picture = oAuth2User.getAttribute("picture");
		
		Users user  = userRepository.findEmail(email);

		if(user == null) {
			Users u = new Users();
			u.setFullName(name);
			u.setEmail(email);
			u.setImage(picture);
			u.setPassword(passwordEncoder.encode(RamDomNameUtils.generateRandomPassword()));
			u.setDate_insert(new Date());
			u.setDate_update(new Date());
			userRepository.save(u);
			
			//set quyền
			user = new Users();
			user = userRepository.findEmail(email);
			Role roleId = new Role();
			roleId.setId("USER");
			Users userid = new Users();
			userid.setId(user.getId());
			Authority authority = new Authority();
			authority.setUser(userid);
			authority.setRole(roleId);
			authorityRepository.save(authority);

			UserDetails userDetails = User.withUsername(user.getEmail()).password(passwordEncoder.encode(RamDomNameUtils.generateRandomPassword())).roles(roleId.getId()).build();
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			session.set("user", user);
		}else {
			user = userRepository.findEmail(email);
			UserInfoUserDetails userDetails = new UserInfoUserDetails(user);
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			System.out.println(userDetails.getAuthorities());
			session.set("user", user);
		}
			
		
		
	}

}
