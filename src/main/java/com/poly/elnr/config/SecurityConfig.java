package com.poly.elnr.config;

import com.poly.elnr.security.CustomAuthenticationFailureHandler;
import com.poly.elnr.security.CustomAuthenticationSuccessHandler;
import com.poly.elnr.security.CustomLogoutSuccessHandler;
import com.poly.elnr.security.oauth2.CustomOAuth2SuccessHandler;
import com.poly.elnr.security.oauth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.poly.elnr.service.serviceImpl.UserSecurityService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public UserDetailsService userDetailsService() {
		return new UserSecurityService();
    }

	@Autowired
	private CustomOAuth2UserService oauth2UserService;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
					.authorizeHttpRequests()
					.requestMatchers("/assets/user/**","/user/new").permitAll()
					.requestMatchers("/client/**").hasAuthority("ROLE_USER")
//					.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
					.anyRequest().permitAll()
					.and()
					.formLogin()
					.loginPage("/security/user")
					.loginProcessingUrl("/j_spring_security_check")
					.successHandler(authenticationSuccessHandler())
					.failureHandler(simpleUrlAuthenticationFailureHandler())


					.and()
					.logout()
					.logoutUrl("/logout")
		            .logoutSuccessHandler(logoutSuccessHandler())
		            .permitAll()

		            .and()
		            .oauth2Login()
	                .loginPage("/security/user")
					.userInfoEndpoint()
					.userService(oauth2UserService)
					.and()
					.successHandler(oauth2SuccessHandler());
		return http.build();
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
	 public SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler(){
		return new CustomAuthenticationFailureHandler();
	 }

     @Bean
     public AuthenticationSuccessHandler authenticationSuccessHandler() {
         return new CustomAuthenticationSuccessHandler();
     }

	@Bean
	public AuthenticationSuccessHandler oauth2SuccessHandler() {
		return new CustomOAuth2SuccessHandler();
	}

     
     @Bean
     public LogoutSuccessHandler logoutSuccessHandler() {
         return new CustomLogoutSuccessHandler();
     }
     
}
