package com.api.tuto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/raw/v1")
public class RawController {

	
	@GetMapping("/test")
	public String test () {
		return "hello";
	}
}
