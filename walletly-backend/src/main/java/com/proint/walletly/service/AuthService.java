package com.proint.walletly.service;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proint.walletly.model.Role;
import com.proint.walletly.model.User;
import com.proint.walletly.repository.UserRepository;
import com.proint.walletly.utils.JwtUtils;
import com.proint.walletly.utils.LoginRequest;
import com.proint.walletly.utils.SignupRequest;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    
    private UserRepository userRepository;
    
    private PasswordEncoder passwordEncoder;
    
    private final JwtUtils jwtUtil;
    
    private final RoleService roleService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtil, RoleService roleService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), 
                        loginRequest.password())
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }
    
    public String register(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.username())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        if (userRepository.existsByEmail(signupRequest.email())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        User user = User.builder()
                .username(signupRequest.username())
                .nome(signupRequest.nome())
                .email(signupRequest.email())
                .password(passwordEncoder.encode(signupRequest.password()))
                .build();
        
        Role userRole = roleService.createRoleIfNotExists("ROLE_USER");
        user.addRole(userRole);
        
        userRepository.save(user);
        return "User registered successfully!";
    }

    public User getAuthenticatedUser() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user");
    }
}
