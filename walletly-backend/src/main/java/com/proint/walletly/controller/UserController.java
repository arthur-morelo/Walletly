package com.proint.walletly.controller;

import com.proint.walletly.model.User;
import com.proint.walletly.model.enums.RoleEnum;
import com.proint.walletly.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String roleStr = body.get("role");
        if (roleStr == null) {
            return ResponseEntity.badRequest().body("Role é obrigatória");
        }

        try {
            RoleEnum newRole = RoleEnum.valueOf(roleStr.toUpperCase());
            User user = userOpt.get();
            user.setRole(newRole);
            userRepository.save(user);
            return ResponseEntity.ok("Role do usuário atualizada com sucesso para " + newRole.name());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Role inválida");
        }
    }

    @PutMapping("/ativar-plano-teste")
    public ResponseEntity<?> ativarPlanoTeste() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setRole(RoleEnum.PAID); // Define a role como PAID
        userRepository.save(usuario);
        return ResponseEntity.ok().build();
    }
}
