package com.securitydemo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {
	
	@GetMapping("/good-login")
	public String goodLogin(@AuthenticationPrincipal UserDetails userDetails) {
		String role = userDetails.getAuthorities().toString();
		String displayRole = role.contains("admin") ? "Admin" : "a regular user";
		String username = userDetails.getUsername();
		return "<html><head><title>Successful Login</title><style>"
				+ "body { background-color: FFCD00; color: 000000; font-family: 'Lato', sans-serif; }"
				+ " </style></head>"
				+ "<body><h1>You are logged in as " + displayRole + ".</h1>"
				+ "<h2>Your username is: " + username + ".</h2>"
				+ "<p><a href='/login.html'>Login as different user</a></p>"
				+ "<p><a href='/public-info'>Public Info</a></p>"
				+ "<p><a href='/admin-only'>Admin Only</a></p>"
				+ "<img src='https://i.imgur.com/2LmNOXn.png'></body></html>";
	}

	@GetMapping("/public-info")
	public String publicInfo() {
		return "<html><head><title>Public Info</title><style>"
				+ "body { background-color: FFCD00; color: 000000; font-family: 'Lato', sans-serif; }"
				+ "</style></head>"
				+ "<body><h1>Public Info</h1>"
				+ "<p>This content is accessible to both admins and regular users.</p></body></html>";
	}
	
	@GetMapping("/admin-only")
	public String adminOnly(@AuthenticationPrincipal UserDetails userDetails) {
//		String username = userDetails.getUsername();
		return "<html><head><title>Admin Only</title><style>"
				+ "body { background-color: FFCD00; color: 000000; font-family: 'Lato', sans-serif; }"
				+ "</style></head><body><h1>Admin Page</h1>"
				+ "<p>Download the database file here: <a href='/download-db'>Link</a>. " 
				+ "Download the log file here: <a href='/download-log'>Link</a>.</p>"
				+ "</body></html>";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied() {
		return "<html><head><title>Access Denied</title></head>"
				+ "<body><h1>Forbidden</h1><p>You don't have permission to access this resource.</p></body></html>";
	}
}
