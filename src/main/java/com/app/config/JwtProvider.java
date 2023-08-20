package com.app.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	
	SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	//to generate token
	public String generateToken(Authentication auth) {
		String jwt=Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+846000000))  //846000000=24hrs -- token will expire in 24hrs
				.claim("email", auth.getName())
				.signWith(key).compact();
		
			return jwt;	
	}
	
	//to claim email from token
	public String getEmailFromToken(String jwt)
	{
		jwt=jwt.substring(7);
		
		Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(jwt).getBody();
		
		String email=String.valueOf(claims.get("email"));
		
		return email;
	}
	
}
