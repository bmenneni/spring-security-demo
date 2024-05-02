package com.securitydemo.services;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

	private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);
	private static final int MAX_ATTEMPTS = 5;
	private static final long BLOCK_TIME = 1000*60*10;
	
	private ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, LocalDateTime> blockTimeCache = new ConcurrentHashMap<>();
	
	public void loginFailed(String key) {
		int attempts = attemptsCache.getOrDefault(key, 0);
		attempts++;
		attemptsCache.put(key, attempts);
		log.warn("Failed login attempt for {}: count = {}", key, attempts);
		if (attempts == MAX_ATTEMPTS) {
			blockTimeCache.put(key, LocalDateTime.now());
		}
	}
	
	public void loginSucceeded(String key) {
		attemptsCache.remove(key);
		blockTimeCache.remove(key);
	}
	
	public boolean isBlocked(String key) {
		if(blockTimeCache.containsKey(key)) {
			LocalDateTime blockTime = blockTimeCache.get(key);
			if(blockTime.plusSeconds(BLOCK_TIME / 1000).isAfter(LocalDateTime.now())) {
				return true;
			} else {
				blockTimeCache.remove(key);
				attemptsCache.remove(key);
			}
		}
		return false;
	}

}
	

