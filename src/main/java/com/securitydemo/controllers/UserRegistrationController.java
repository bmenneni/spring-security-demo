package com.securitydemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.securitydemo.models.User;
import com.securitydemo.services.UserService;
import com.securitydemo.dto.RegistrationRequest;

@RestController
public class UserRegistrationController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
		try {
			User user = userService.registerNewUser(request.getUsername(), request.getPassword());
			return ResponseEntity.ok(user);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
