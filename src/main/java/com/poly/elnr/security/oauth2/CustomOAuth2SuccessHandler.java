package com.poly.elnr.security.oauth2;

import com.poly.elnr.entity.Users;
import com.poly.elnr.service.UserService;
import com.poly.elnr.service.serviceImpl.SessionService;
import com.poly.elnr.utils.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;
    @Autowired
    SessionService session;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        UserDetails userDetails = userService.oauth2(oauthUser);

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/user/index");
                return;

            } else if (authority.getAuthority().equals("ROLE_USER")) {
                response.sendRedirect("/user/index");
                return;

            }
//            } else if (authority.getAuthority().equals("SELLER")) {
//
//                response.sendRedirect("/seller/home");
//                return;
//            }
        }
    }
}
