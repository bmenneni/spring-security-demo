package com.securitydemo.controllers;

import org.springframework.core.io.InputStreamResource;
//import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DownloadController {
	
	@GetMapping("/download-db")
	public ResponseEntity<?> downloadDatabase(HttpServletRequest request) throws IOException {
		String referrer = request.getHeader("Referer");
		if (referrer == null || !referrer.endsWith("/admin-only")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("403 Forbidden");
		}
		
		File file = new File("securitydemo.db");
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=securitydemo.db")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(file.length())
				.body(resource);
	}
	
	@GetMapping("/download-log")
	public ResponseEntity<?> downloadLog(HttpServletRequest request) throws IOException {
		String referrer = request.getHeader("Referer");
		if (referrer == null || !referrer.endsWith("/admin-only")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("403 Forbidden");
		}
		
		File file = new File("logs/securitydemo.log");
		InputStreamResource resource = new InputStreamResource( new FileInputStream(file));
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=securitydemo.log")
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(file.length())
				.body(resource);
	}

}
