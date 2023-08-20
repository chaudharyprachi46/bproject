 package com.app.config;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

//import jakarta.servlet.http.HttpServletRequest;



@Configuration
public class AppConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeHttpRequests(authorize -> authorize
	                .antMatchers("/api/**").authenticated() // URLs starting with "/api/" require authentication
	                .anyRequest().permitAll())  //url which are allowed i.e starting from api
			.addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
			.csrf().disable()
			.cors().configurationSource(new CorsConfigurationSource() {

				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					
					CorsConfiguration cfg = new CorsConfiguration(); 
					// the websites which we need to allow
					cfg.setAllowedOrigins(Arrays.asList(
							"http://localhost:3000" //react
							));
					cfg.setAllowedMethods(Collections.singletonList("*")); // all (get/put/post..... allowed)   //Collections.
					cfg.setAllowCredentials(true);
					cfg.setExposedHeaders(Arrays.asList("Authorization"));
					cfg.setMaxAge(3600L);
					return cfg;
				}
				
			})
			.and().httpBasic().and().formLogin();
		
		return http.build();
	}
	
//	//to decrypt password -- to hash paswrd
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
}
 