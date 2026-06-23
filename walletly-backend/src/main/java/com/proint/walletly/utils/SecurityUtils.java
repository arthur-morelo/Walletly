package com.proint.walletly.utils;

import com.proint.walletly.model.User;
import com.proint.walletly.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Usuário não está autenticado");
        }
        
        String username = authentication.getName(); // getName returns username or email based on the UserDetailsService
        
        // Tentamos primeiro pelo username, depois pelo email, pois o JWT pode ter armazenado qualquer um deles
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado (" + username + ") não encontrado")));
    }
}
