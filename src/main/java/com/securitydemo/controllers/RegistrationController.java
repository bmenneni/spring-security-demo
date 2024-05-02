package com.securitydemo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.securitydemo.services.UserService;

@RestController
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/good-registration")
	public String goodRegistration(HttpServletRequest request) {
		String referrer = request.getHeader("Referer");
		if (referrer == null || !referrer.endsWith("/register.html")) {
			return "403 Forbidden";
		}
		return "<html><head><title>Successful Registration</title></head>"
				+ "<p>Registration successful! Please log in.</p>"
				+ "<p><a href='/login.html'>Return to Login</a></p></html>";
	}
	
	@PostMapping("/registration")
	public ModelAndView registerUser(
			@RequestParam("username") String username,
			@RequestParam("password") String password) {
		try {
			userService.registerNewUser(username, password);
			return new ModelAndView("redirect:/good-registration");
		} catch (IllegalArgumentException e) {
			ModelAndView modelAndView = new ModelAndView("register");
			modelAndView.addObject("message", "Error: " + e.getMessage());
			return modelAndView;
		}
	}
}
