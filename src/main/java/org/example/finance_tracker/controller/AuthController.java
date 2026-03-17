package org.example.finance_tracker.controller;

import org.example.finance_tracker.dto.LoginResponse;
import org.example.finance_tracker.dto.LoginUserDto;
import org.example.finance_tracker.dto.RegisterUserDto;
import org.example.finance_tracker.entity.UserEntity;
import org.example.finance_tracker.service.AuthentificationService;
import org.example.finance_tracker.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthentificationService authentificationService;
    private final JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);



    public AuthController(AuthentificationService authentificationService, JwtService jwtService) {
        this.authentificationService = authentificationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(
            @RequestBody RegisterUserDto registerUserDto
    ) {
        UserEntity registeredUser = authentificationService.signUp(registerUserDto);

        log.info("register user by email = {}  ; password = {}",registeredUser.getEmail(),registeredUser.getPassword());
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginUserDto loginUserDto
    ) {
        log.info("authenticate user by {}",loginUserDto.toString());
        UserEntity authenticatedUser = authentificationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        log.info("login response user by {}",loginResponse.toString());
        return ResponseEntity.ok(loginResponse);
    }
}

