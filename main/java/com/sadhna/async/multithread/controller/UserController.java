package com.sadhna.async.multithread.controller;


import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sadhna.async.multithread.entity.User;
import com.sadhna.async.multithread.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
	public ResponseEntity SaveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception
	{
		for(MultipartFile file: files)
		{
			userService.saveUser(file);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
       return  userService.findAllUser().thenApply(ResponseEntity::ok);
    }
	
	@GetMapping(value = "/getUsersByThread", produces = "application/json")
	public ResponseEntity getUser()
	{
		CompletableFuture<List<User>> users1 = userService.findAllUser();
		CompletableFuture<List<User>> users2 = userService.findAllUser();
		CompletableFuture<List<User>> users3 = userService.findAllUser();
		
		CompletableFuture.allOf(users1, users2,users3).join();
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
	

}
