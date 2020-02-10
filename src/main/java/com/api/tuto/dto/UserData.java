package com.api.tuto.dto;


import com.api.tuto.model.User;

import lombok.Data;

@Data
public class UserData {

	private String token;
	private User user;
	private Long expiryTimeMillisecs;
	
}
