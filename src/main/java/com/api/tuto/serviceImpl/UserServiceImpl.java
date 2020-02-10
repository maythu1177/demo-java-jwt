package com.api.tuto.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.tuto.config.Response;
import com.api.tuto.dto.UserData;
import com.api.tuto.dto.UserDto;
import com.api.tuto.model.Role;
import com.api.tuto.model.User;
import com.api.tuto.repo.UserRepo;
import com.api.tuto.security.JwtTokenUtil;
import com.api.tuto.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${token.expire.time}")
	private long tokenExpireTime;

	@Override
	public Response register(User user) {
		User result = userRepo.findByEmail(user.getEmail());
		if (result == null) {
			String pwd = user.getPassword();
			String encodedPwd = bcryptPasswordEncoder.encode(pwd);
			user.setPassword(encodedPwd);
			userRepo.save(user);
			return new Response(Boolean.TRUE, "success");
		} else {
			return new Response(Boolean.FALSE, "user with this email  already exist");
		}

	}

	@Override
	public Response login(UserDto dto) {
		String email = dto.getEmail();
		Long expiryTime = tokenExpireTime * 60 * 1000;
		User user = userRepo.findByEmail(email);
		Boolean result = bcryptPasswordEncoder.matches(dto.getPassword(), user.getPassword());
		if (result == true) {
			UserData userData = new UserData();
			userData.setToken(jwtTokenUtil.generateToken(user));
			userData.setUser(user);
			userData.setExpiryTimeMillisecs(expiryTime);
			return new Response(Boolean.TRUE, userData);
		}
		return new Response(Boolean.FALSE, "email or password wrong");
	}

}
