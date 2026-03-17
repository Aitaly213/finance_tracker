package org.example.finance_tracker.service;

import org.apache.coyote.BadRequestException;
import org.example.finance_tracker.dto.LoginUserDto;
import org.example.finance_tracker.dto.RegisterUserDto;
import org.example.finance_tracker.entity.UserEntity;
import org.example.finance_tracker.repository.UserRepository;
import org.hibernate.annotations.NotFound;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthentificationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthentificationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public UserEntity signUp(RegisterUserDto registerUserDto){
        UserEntity user = new UserEntity(registerUserDto.username(),
                registerUserDto.email(),
                passwordEncoder.encode(registerUserDto.password())
        );

        return userRepository.save(user);
    }

    public UserEntity authenticate(LoginUserDto loginUserDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.email(),
                        loginUserDto.password()
                )
        );
        return userRepository.findByEmail(loginUserDto.email()).orElseThrow(() ->
               new NoSuchElementException("Not found user by this email : "+ loginUserDto.email())
        );
    }
}
