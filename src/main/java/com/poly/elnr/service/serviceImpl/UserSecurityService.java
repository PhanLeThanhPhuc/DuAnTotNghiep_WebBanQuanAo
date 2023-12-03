package com.poly.elnr.service.serviceImpl;

import com.poly.elnr.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.poly.elnr.security.CustomUserDetails;
import com.poly.elnr.entity.Users;
import com.poly.elnr.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserSecurityService implements UserDetailsService{

	@Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){

        Users user;

        if(RegexUtils.isPhoneNumber(username)) {
            user = userRepository.findByPhone(username);
            return new CustomUserDetails(user);
        }else if(RegexUtils.isEmail(username)){
            user = userRepository.findByEmail(username);
            return new CustomUserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found: " );
        }
    }
}
