package com.api.tuto.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.tuto.config.Response;
import com.api.tuto.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	MyUserDetailsService userDetailsService;



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		try {
			String username = null;
			String jwtToken = null;

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwtToken = authorizationHeader.substring(7);
				
				jwtTokenUtil.validateToken(jwtToken);
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				chain.doFilter(request, response);

			} else {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				Response resp = new Response(false, "Please provide proper header Value");
				String jsonRespString = ow.writeValueAsString(resp);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				PrintWriter writer = response.getWriter();
				writer.write(jsonRespString);
			}

		} catch (Exception e) {
			e.printStackTrace();
            
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			Response resp = new Response(false, e.getMessage());
			String jsonRespString = ow.writeValueAsString(resp);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			PrintWriter writer = response.getWriter();
			writer.write(jsonRespString);

		}
	}
}
