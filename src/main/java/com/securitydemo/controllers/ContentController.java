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
		return "<html><body><h1>You are logged in as " + displayRole + "</h1>"
				+ "<p><a href='/login'>Login as different user</a></p>"
				+ "<p><a href='/general-info'>General info</a></p>"
				+ "<p><a href='/admin-only'>Admins only</a></p>"
				+ "</body></html>";
	}

	@GetMapping("/general-info")
	public String generalInfo() {
		return "<html><body><h1>Public Info</h1><p>This content is accessible to both admins and regular users.</p></body></html>";
	}
	
	@GetMapping("/admin-only")
	public String adminOnly() {
		return "<html><body><h1>Admin Page</h1><p>This content is only accessible to admins.</p></body></html>";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied() {
		return "<html><body><h1>Access Denied</h1><p>Invalid authorization.</p></body></html>";
	}
}
