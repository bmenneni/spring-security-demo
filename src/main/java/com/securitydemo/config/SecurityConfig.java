package com.securitydemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/register").permitAll()
			.antMatchers("/admin-only").hasAuthority("admin")
			.antMatchers("/general-info").hasAnyAuthority("admin", "reg_user")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.defaultSuccessUrl("/good-login", true)
				.permitAll()
			.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		
		return http.build();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			String username = request.getUserPrincipal().getName();
			String url = request.getRequestURI();
			log.warn("User '{}' attempted to access the protected URL: {}", username, url);
			response.sendRedirect("/access-denied");
		};
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
