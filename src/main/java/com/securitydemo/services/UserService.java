package com.securitydemo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.securitydemo.models.User;
import com.securitydemo.repositories.UserRepository;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public User registerNewUser(String username, String password) {
		if(userRepository.existsByUsername(username)) {
			log.warn("Registration attempt with existing username: {}", username);
			throw new IllegalArgumentException("Username already exists.");
		}
		
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPassword(passwordEncoder.encode(password));
		newUser.setRole("reg_user"); //new users can only register as regular users
		
		log.info("New user registered: {}", username);
		
		return userRepository.save(newUser);
	}
	
}
