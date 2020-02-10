package com.api.tuto.config;

import lombok.Data;

@Data
public class Response {
 
	private Boolean status;
	private String message;
	private Object payload;
	
	public Response(Boolean status, String message, Object payload) {
		this.status = status;
		this.message = message;
		this.payload = payload;
	}
	
	public Response(Boolean status, String message) {
		this.status = status;
		this.message = message;
	}

	public Response(Boolean status, Object payload) {
		this.status = status;
		this.payload = payload;
	}
	
}
