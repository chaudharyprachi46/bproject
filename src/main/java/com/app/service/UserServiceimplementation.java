package com.app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.config.JwtProvider;
import com.app.exception.UserException;
import com.app.model.User;
import com.app.repository.UserRepository;

@Service
public class UserServiceimplementation implements UserService{
	
	private  UserRepository  userRepository;
	private  JwtProvider jwtProvider;
	
	

	public UserServiceimplementation(UserRepository userRepository, JwtProvider jwtProvider) {
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User> user= userRepository.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserException("user not found with id - "+userId);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		String email= jwtProvider.getEmailFromToken(jwt);
		
		User user= userRepository.findByEmail(email);
		if(user==null) {
			throw new UserException("user not found with email : "+email);
		}
		
		return user;
	}

}
