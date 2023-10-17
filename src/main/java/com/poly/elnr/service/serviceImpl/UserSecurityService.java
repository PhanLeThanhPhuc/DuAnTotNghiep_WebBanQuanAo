package com.poly.elnr.service.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.poly.elnr.config.UserInfoUserDetails;
import com.poly.elnr.entity.User;
import com.poly.elnr.repository.UserRepository;



public class UserSecurityService implements UserDetailsService{
	@Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userInfo = repository.findByEmail(email);
        if (userInfo.isPresent()) {
            return new UserInfoUserDetails(userInfo.get());
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }
}
