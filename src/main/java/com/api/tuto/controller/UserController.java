package com.api.tuto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.tuto.config.Response;
import com.api.tuto.dto.UserDto;
import com.api.tuto.model.User;
import com.api.tuto.service.UserService;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {
 
	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public Response register(@RequestBody User user) {
		return userService.register(user);
	}
	
	@PostMapping("/login")
	public Response login (@RequestBody UserDto dto) {
		return userService.login(dto);
	}
	
}
