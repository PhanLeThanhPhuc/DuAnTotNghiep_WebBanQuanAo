package com.poly.elnr.service.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.poly.elnr.config.UserInfoUserDetails;
import com.poly.elnr.entity.Users;
import com.poly.elnr.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserSecurityService implements UserDetailsService{

	@Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users userInfo = repository.findEmail(email);
        if (!(userInfo == null)) {
            return new UserInfoUserDetails(userInfo);
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }
}
