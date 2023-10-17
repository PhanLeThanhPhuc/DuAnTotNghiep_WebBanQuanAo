package com.poly.elnr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.poly.elnr.service.serviceImpl.UserSecurityService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
    // authentication
    public UserDetailsService userDetailsService() {
		return new UserSecurityService();
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf().disable()
					.authorizeHttpRequests()
					.requestMatchers("/assets/user/**","/user/new").permitAll()
					.requestMatchers("/cline/**").hasAuthority("ROLE_USER")
					.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
					.anyRequest().permitAll()
					.and()
					.formLogin()
					.loginPage("/security/user")
					.loginProcessingUrl("/j_spring_security_check")
					.successHandler(authenticationSuccessHandler()) 
					.failureUrl("/security/user?error=fail")
					
					.and()
					.logout()
					.logoutUrl("/logout") 
		            .logoutSuccessHandler(logoutSuccessHandler()) // Xử lý sau khi đăng xuất thành công
		            .permitAll()
		            
		            .and()
		            .oauth2Login()
	                .loginPage("/security/user")
	                .defaultSuccessUrl("/user/controller", true).and()
		            .build();

	}
    
	
//	 @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//	        http.sessionManagement()
//	            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
//	    }
	
	

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
     @Bean
     public AuthenticationProvider authenticationProvider(){
         DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
         authenticationProvider.setUserDetailsService(userDetailsService());
         authenticationProvider.setPasswordEncoder(passwordEncoder());
         return authenticationProvider;
     }

     @Bean
     public AuthenticationSuccessHandler authenticationSuccessHandler() {
         return new CustomAuthenticationSuccessHandler();
     }
     
     @Bean
     public LogoutSuccessHandler logoutSuccessHandler() {
         return new CustomLogoutSuccessHandler();
     }
     
}
