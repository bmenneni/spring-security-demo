package com.securitydemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/register.html", "/registration", "/good-registration", "/login.html", "/submit-login").permitAll()
			.antMatchers("/admin-only", "/download-db", "/download-log").hasAuthority("admin")
			.antMatchers("/public-info").hasAnyAuthority("admin", "reg_user")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login.html")
				.loginProcessingUrl("/submit-login")
				.failureUrl("/login.html?bad-login")
				.defaultSuccessUrl("/good-login", true)
				.permitAll()
			.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
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
