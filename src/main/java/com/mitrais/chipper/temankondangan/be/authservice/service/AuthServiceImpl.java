package com.mitrais.chipper.temankondangan.be.authservice.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mitrais.chipper.temankondangan.be.authservice.exception.BadRequestException;
import com.mitrais.chipper.temankondangan.be.authservice.exception.UnauthorizedException;
import com.mitrais.chipper.temankondangan.be.authservice.model.User;
import com.mitrais.chipper.temankondangan.be.authservice.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	public static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

	PasswordEncoder passwordEncoder;
	UserRepository userRepository;

	@Autowired
	public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	@Override
	public boolean login(String email, String password) {
		boolean result;
		User data = userRepository.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedException("Error: Username or password not match"));

		if (password != null) {
			result = passwordEncoder.matches(password, data.getPasswordHashed());
		} else {
			throw new BadRequestException("Error: Password cannot be empty");
		}

		if (!result) {
			throw new UnauthorizedException("Error: Username or password not match");
		}
		return true;
	}

	@Override
	public boolean logout(Long userId) {
		User data = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Error: User Not Found"));

		data.setLogout(new Date());
		data.setMessagingToken(null);
		userRepository.save(data);

		return true;
	}
}
