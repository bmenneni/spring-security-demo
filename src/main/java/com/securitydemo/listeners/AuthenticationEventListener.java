package com.securitydemo.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.securitydemo.services.LoginAttemptService;

@Component
public class AuthenticationEventListener {

	@Autowired
	private LoginAttemptService loginAttemptService;
	
	@EventListener
	public void handleAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
		WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
		String username = event.getAuthentication().getName();
		String ip = details.getRemoteAddress();
		String key = username + ":" + ip;
		loginAttemptService.loginFailed(key);
	}
	
	@EventListener
	public void handleAuthenticationSuccess(AuthenticationSuccessEvent event)  {
		WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
		String username = event.getAuthentication().getName();
		String ip = details.getRemoteAddress();
		String key = username + ":" + ip;
		loginAttemptService.loginSucceeded(key);			
	}
	
}
