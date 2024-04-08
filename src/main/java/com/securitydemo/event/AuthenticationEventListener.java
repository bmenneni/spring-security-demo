package com.securitydemo.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.securitydemo.services.LoginAttemptService;

@Component
public class AuthenticationEventListener {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationEventListener.class);

	@Autowired
	private LoginAttemptService loginAttemptService;
	
	@EventListener
	public void handleAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
		String username = event.getAuthentication().getName();
		//here, need to extract the IP address from the request somehow
		Object source = event.getAuthentication().getDetails();
		if (source instanceof WebAuthenticationDetails) {
			WebAuthenticationDetails details = (WebAuthenticationDetails) source;
			String ip = details.getRemoteAddress();
			loginAttemptService.loginFailed(username, ip);
			log.warn("Failed login attempt for username: {} from IP: {}", username, ip);
		}
	}
	
	@EventListener
	public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
		String username = event.getAuthentication().getName();
		//extract IP address here
		Object source = event.getAuthentication().getDetails();
		if (source instanceof WebAuthenticationDetails) {
			WebAuthenticationDetails details = (WebAuthenticationDetails) source;
			String ip = details.getRemoteAddress();
			loginAttemptService.loginSucceeded(username, ip);
		}
	}
}
