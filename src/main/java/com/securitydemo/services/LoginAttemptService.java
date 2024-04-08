package com.securitydemo.services;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

	private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);
	private static final int MAX_ATTEMPTS = 5;
//	private static final long BLOCK_TIME = 1000*60*15; 
	
	private Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
	private Map<String, LocalDateTime> blockTimeCache = new ConcurrentHashMap<>();
	
	public void loginFailed(String username, String ip) {
		String key = username + ":" + ip;
		attemptsCache.put(key, attemptsCache.getOrDefault(key, 0) + 1);
		blockTimeCache.putIfAbsent(key, LocalDateTime.now());
		
		int attemptCount = attemptsCache.get(key);
		if(attemptCount >= MAX_ATTEMPTS) {
			throw new RuntimeException("User is blocked due to too many failed attempts.");
		} else {
			log.debug("Failed login attempt for {}: count = {}", key, attemptCount);	
		}
	}
	
	public void loginSucceeded(String username, String ip) {
		String key = username + ":" + ip;
		attemptsCache.remove(key);
		blockTimeCache.remove(key);
	}
	
	public boolean isBlocked(String username, String ip) {
		String key = username + ":" + ip;
		boolean isBlocked = attemptsCache.getOrDefault(key, 0) >= MAX_ATTEMPTS;
		log.debug("Is blocked check for {}: {}", key, isBlocked);
		return isBlocked;
				
//		LocalDateTime blockTime = blockTimeCache.get(key);
		
//		if (blockTime == null) {
//			return false;
//		}
//		
//		if (LocalDateTime.now().isBefore(blockTime.plusSeconds(BLOCK_TIME / 1000))) {
//			return attemptsCache.getOrDefault(key, 0) >= MAX_ATTEMPTS;
//		} else {
//			loginSucceeded(username, ip);
//			return false;
//		}	
		
	}
}
