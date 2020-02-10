package com.api.tuto.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.tuto.config.Response;
import com.api.tuto.model.User;
import com.api.tuto.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserRepo userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		try {

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				String jwtToken = authorizationHeader.substring(7);

				jwtTokenUtil.validateToken(jwtToken);
				String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

				User userDetails = userRepo.findByEmail(username);
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails,null,Arrays.asList(
		        				new SimpleGrantedAuthority(userDetails.getRole().name())));
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			}

		} catch (Exception e) {
			System.out.println("message"+e.getMessage());
			e.printStackTrace();

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			Response resp = new Response(false, e.getMessage());
			String jsonRespString = ow.writeValueAsString(resp);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			PrintWriter writer = response.getWriter();
			writer.write(jsonRespString);

		}
		
		chain.doFilter(request, response);
	}
}


