package com.poly.elnr.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.security.auth.login.AccountExpiredException;
import java.io.IOException;
import java.net.URLEncoder;


@Configuration
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
        String errorMessage = "";

        if (exception instanceof BadCredentialsException) {
            errorMessage += "Sai tài khoản hoặc mật khẩu";
        } else if (exception instanceof LockedException) {
            errorMessage += "Your account is locked. Please contact the administrator.";
        } else if (exception instanceof DisabledException) {
            errorMessage += "Your account is disabled. Please contact the administrator.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "Your credentials have expired. Please update your password.";
        }

        response.sendRedirect("/user/login?error=fail&message=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }

}



