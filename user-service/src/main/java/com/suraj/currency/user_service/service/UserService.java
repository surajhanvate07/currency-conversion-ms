package com.suraj.currency.user_service.service;

import com.suraj.currency.user_service.dto.LoginRequestDto;
import com.suraj.currency.user_service.dto.SignUpRequestDto;
import com.suraj.currency.user_service.dto.UserDto;
import com.suraj.currency.user_service.entity.User;
import com.suraj.currency.user_service.exception.AlreadyExistsException;
import com.suraj.currency.user_service.exception.InvalidCredentialException;
import com.suraj.currency.user_service.exception.ResourceNotFoundException;
import com.suraj.currency.user_service.repository.UserRepository;
import com.suraj.currency.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final JwtService jwtService;

	public UserDto signUp(SignUpRequestDto signUpRequestDto) {
		log.info("Signing up user with email: {}", signUpRequestDto.getEmail());
		User user = modelMapper.map(signUpRequestDto, User.class);

		// Hash the password before saving
		user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

		// Check if the email already exists
		if (userRepository.existsByEmail(user.getEmail())) {
			log.error("Email already exists: {}", user.getEmail());
			throw new AlreadyExistsException("Email already exists: " + user.getEmail());
		}

		try {
			User savedUser = userRepository.save(user);
			log.info("User created successfully with ID: {}", savedUser.getId());
			return modelMapper.map(savedUser, UserDto.class);
		} catch (Exception e) {
			log.error("Error occurred while creating user: {}", e.getMessage());
			throw new RuntimeException("User creation failed");
		}
	}

	public String login(LoginRequestDto loginRequestDto) {
		log.info("Logging in user with email: {}", loginRequestDto.getEmail());
		User user = userRepository.findByEmail(loginRequestDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequestDto.getEmail()));

		// Check if the password matches
		log.info("Checking password matches for user: {}", user.getEmail());
		boolean isPasswordMatches = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

		if (!isPasswordMatches) {
			log.error("Invalid password for user: {}", user.getEmail());
			throw new InvalidCredentialException("Invalid username or password");
		}

		return jwtService.generateAccessToken(user);
	}
}
