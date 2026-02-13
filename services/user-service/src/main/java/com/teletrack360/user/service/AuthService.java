package com.teletrack360.user.service;

import com.teletrack360.common.enums.UserRole;
import com.teletrack360.common.exception.ResourceNotFoundException;
import com.teletrack360.common.exception.UnauthorizedException;
import com.teletrack360.common.exception.ValidationException;
import com.teletrack360.user.dto.*;
import com.teletrack360.user.entity.RefreshToken;
import com.teletrack360.user.entity.User;
import com.teletrack360.user.repository.RefreshTokenRepository;
import com.teletrack360.user.repository.UserRepository;
import com.teletrack360.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username already exists");
        }
        
        // Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        
        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole() != null ? request.getRole() : UserRole.OPERATOR)
                .status("ACTIVE")
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getUsername());
        
        // Generate tokens
        UserDetails userDetails = createUserDetails(savedUser);
        String accessToken = jwtService.generateToken(userDetails);
        String refreshTokenStr = jwtService.generateRefreshToken(userDetails);
        
        // Save refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(refreshToken);
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .user(mapToUserResponse(savedUser))
                .build();
    }
    
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            // Find user
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
            
            // Update last login
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Generate tokens
            UserDetails userDetails = createUserDetails(user);
            String accessToken = jwtService.generateToken(userDetails);
            String refreshTokenStr = jwtService.generateRefreshToken(userDetails);
            
            // Revoke old refresh tokens
            refreshTokenRepository.revokeAllByUser(user, LocalDateTime.now());
            
            // Save new refresh token
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(refreshTokenStr)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusDays(7))
                    .createdAt(LocalDateTime.now())
                    .build();
            refreshTokenRepository.save(refreshToken);
            
            log.info("User logged in: {}", user.getUsername());
            
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenStr)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getExpirationTime())
                    .user(mapToUserResponse(user))
                    .build();
            
        } catch (Exception e) {
            log.error("Login failed for username: {}", request.getUsername());
            throw new UnauthorizedException("Invalid credentials");
        }
    }
    
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        
        if (refreshToken.isRevoked() || refreshToken.isExpired()) {
            throw new UnauthorizedException("Refresh token expired or revoked");
        }
        
        User user = refreshToken.getUser();
        UserDetails userDetails = createUserDetails(user);
        
        // Generate new access token
        String newAccessToken = jwtService.generateToken(userDetails);
        
        log.info("Token refreshed for user: {}", user.getUsername());
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .user(mapToUserResponse(user))
                .build();
    }
    
    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
        refreshTokenRepository.revokeAllByUser(user, LocalDateTime.now());
        log.info("User logged out: {}", username);
    }
    
    private UserDetails createUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
    
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}
