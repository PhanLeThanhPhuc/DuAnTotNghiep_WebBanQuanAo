package com.poly.elnr.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;


@Configuration
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
        String errorMessage = "";

        if (exception instanceof BadCredentialsException) {
            errorMessage += "Username or password is incorrect.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage += "Username does not exist.";
        } else {
            errorMessage += "There were other authentication errors.";
        }
        response.sendRedirect("/user/login?error=fail&message=" + errorMessage);
    }

}



