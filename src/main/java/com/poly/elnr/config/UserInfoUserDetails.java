package com.poly.elnr.config;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.poly.elnr.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails(Users userInfo) {
    	name = userInfo.getEmail();
        password = userInfo.getPassword();

        authorities = new ArrayList<>();
        userInfo.getAuthorities().forEach(listAuthorities->{
            authorities.add(new SimpleGrantedAuthority(listAuthorities.getRole().getId()));
        });

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return name;
	}

}
