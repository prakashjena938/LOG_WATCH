package com.prakash.LogFetch.service;

import com.prakash.LogFetch.config.JwtUtil;
import com.prakash.LogFetch.dto.AuthResponse;
import com.prakash.LogFetch.dto.User;
import com.prakash.LogFetch.model.UserRegistry;
import com.prakash.LogFetch.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserRegistry register(User user) {
        repo.findByUsername(user.getUsername()).ifPresent(existing -> {
            throw new IllegalArgumentException("Username already taken");
        });

        UserRegistry userRegistry = new UserRegistry();
        userRegistry.setGmail(user.getGmail());
        userRegistry.setUsername(user.getUsername());
        userRegistry.setPassword(passwordEncoder.encode(user.getPassword()));

        return repo.save(userRegistry);
    }

    public AuthResponse login(User user) {
        // Throws BadCredentialsException if the username/password don't match.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, user.getUsername());
    }
}
