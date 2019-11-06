package com.api.tuto.service;

import com.api.tuto.config.Response;
import com.api.tuto.dto.UserDto;
import com.api.tuto.model.User;

public interface UserService  {

	public Response register(User user);
	
	public Response  login(UserDto  user);

}
