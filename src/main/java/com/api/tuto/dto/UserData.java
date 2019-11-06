package com.api.tuto.dto;


import lombok.Data;

@Data
public class UserData {

	private String token;
	private String name;
	private String email;
	private String phone;
	private Long expiryTimeMillisecs;
	
}
