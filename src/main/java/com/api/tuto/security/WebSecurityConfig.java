package com.api.tuto.security;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	
	private String[] PUBLIC_RESOURCE_AND_URL = { "/", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
			"/configuration/**", "/swagger-ui.html", "/webjars/**", "/api/user/v1/login","/api/user/v1/register"};

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers(PUBLIC_RESOURCE_AND_URL);
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.addAllowedOrigin("*");
				config.setAllowCredentials(true);
				return config;
			}
		});
		
		
		http.csrf().disable().authorizeRequests().antMatchers("/api/raw/v1/**").hasAnyRole("USER","SUPER_ADMIN").
		   anyRequest().authenticated().and()
				.exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				and()
		.addFilterBefore( jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
		
	}

}
