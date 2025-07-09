package com.suraj.currency.user_service.controller;

import com.suraj.currency.user_service.dto.LoginRequestDto;
import com.suraj.currency.user_service.dto.SignUpRequestDto;
import com.suraj.currency.user_service.dto.UserDto;
import com.suraj.currency.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/auth")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Add methods to handle user-related requests, such as registration, login, etc.
	@PostMapping("/signup")
	public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
		UserDto createdUser = userService.signUp(signUpRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
		String token = userService.login(loginRequestDto);
		return ResponseEntity.ok(token);
	}
}
