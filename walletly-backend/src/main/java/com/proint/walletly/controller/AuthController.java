package com.proint.walletly.controller;

import java.util.HashMap;
import java.util.Map;

import com.proint.walletly.model.User;
import com.proint.walletly.repository.UserRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proint.walletly.service.AuthService;
import com.proint.walletly.utils.LoginRequest;
import com.proint.walletly.utils.SignupRequest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String jwtToken = authService.login(loginRequest);
            User user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(3600) // 1 hour
                    .sameSite("None")
                    .build();
            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", jwtToken);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("name", user.getNome());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            responseBody.put("user", userInfo);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(responseBody);
        } catch (Exception e) {
            System.err.println("=== ERRO DE LOGIN ===");
            System.err.println("Classe da Exceção: " + e.getClass().getName());
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody SignupRequest signupRequest) {
        System.out.println("\n\n>>> CHEGOU NO CONTROLLER DE REGISTRO! Email: " + signupRequest.email() + " <<<\n\n");
        try {
            String message = authService.register(signupRequest);
            return ResponseEntity.ok("Usuário cadastrado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout successful");
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Funciona pae");
    }
}
