package com.sadhna.async.multithread.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sadhna.async.multithread.entity.User;
import com.sadhna.async.multithread.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	Object target;
	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Async
	public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception{
		
		long start = System.currentTimeMillis();
		List<User> users = parseCSVFile(file);
		logger.info("saving list of user "+ users.size()+ " "+ Thread.currentThread().getName());
		users = userRepository.saveAll(users);
		
		long end =  System.currentTimeMillis();
		logger.info("total time: "+ (end-start));
		
		return CompletableFuture.completedFuture(users);
	}
	
	public CompletableFuture<List<User>> findAllUser()
	{
		logger.info("get list of users by "+ Thread.currentThread().getName());
		
		List<User> users = userRepository.findAll();
		
		return CompletableFuture.completedFuture(users);
	}
	private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user);
                }
                return users;
            }
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
	
	
}
