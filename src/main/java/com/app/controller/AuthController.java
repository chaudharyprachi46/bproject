package com.app.controller;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.config.JwtProvider;
import com.app.exception.UserException;
import com.app.model.User;
import com.app.repository.UserRepository;
import com.app.request.LoginRequest;
import com.app.response.AuthResponse;
import com.app.service.CustomUserServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	
	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	private PasswordEncoder passwordEncoder; //hash the password
	private CustomUserServiceImplementation customUserService;
	
	
	public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder,
			CustomUserServiceImplementation customUserService) {
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
		this.passwordEncoder = passwordEncoder;
		this.customUserService = customUserService;
	}

	//signUp/register a user
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user)throws UserException
	{
		String email=user.getEmail();
		String password=user.getPassword();
		String firstName=user.getFirstName();
		String lastName=user.getLastName();
		
		User isEmailExist= userRepository.findByEmail(email);
		
		//check if a user already exsits with the given email 
		if(isEmailExist!=null) {
			throw new UserException("Email is already in used with another account");
		}
		

		//set/create a new user 
		User createdUser= new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));  //to encode the password 
		createdUser.setFirstName(firstName);
		createdUser.setLastName(lastName);
		
		//saving the newly created user
		User savedUser = userRepository.save(createdUser);
		
		Authentication authentication= new UsernamePasswordAuthenticationToken(savedUser.getEmail() , savedUser.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//get token
		String token = jwtProvider.generateToken(authentication);
		
		//it was getting confused with the token and string as 1st we had passed both token n message in string, 
		//so created getter and setter in authResponse 
		AuthResponse authResponse= new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signup Success");
		
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
		
	}

	// signIn 
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHnadler(LoginRequest loginRequest)
	{
//		String username= loginRequest.getEmail();
//		String password= loginRequest.getPassword();
//		
//		//set authentication in security context
//		Authentication authentication= authenticate(username,password);
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		
//		//generate token and return authResponse
//		String token = jwtProvider.generateToken(authentication);
//		
//		AuthResponse authResponse= new AuthResponse(token, "SignIn Success");
//		
//		
//		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
//		
		String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Authenticate user using custom authentication method
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // No JWT token generation, return response with a message
        AuthResponse authResponse = new AuthResponse();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}



	//to authenticate a user using password, if error throw exception
	private Authentication authenticate(String username, String password) {
		
		//getting user by username to authenticate/compare
		UserDetails userDetails = customUserService.loadUserByUsername(username);  
		
		//if username invalid throw exception
		if(userDetails==null) {
			throw new BadCredentialsException("invalid Username....");
		}
		
		//if passwrd matches it returns true -- using ! so when not matched will return true  
		if(!passwordEncoder.matches(password, userDetails.getPassword()) )
		{
			throw new BadCredentialsException("invalid Password...."); 
		}
		
		//return authorized user
		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}

}








