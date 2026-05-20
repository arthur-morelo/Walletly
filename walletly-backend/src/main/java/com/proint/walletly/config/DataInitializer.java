package com.proint.walletly.config;

import com.proint.walletly.model.User;
import com.proint.walletly.model.enums.RoleEnum;
import com.proint.walletly.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "arthurmorelo@gmail.com";
        Optional<User> adminUserOpt = userRepository.findByEmail(adminEmail);

        if (adminUserOpt.isPresent()) {
            User adminUser = adminUserOpt.get();
            boolean changed = false;
            
            if (adminUser.getRole() != RoleEnum.ADMIN) {
                adminUser.setRole(RoleEnum.ADMIN);
                changed = true;
            }
            
            // Força a atualização da senha se ela não estiver criptografada (BCrypt começa com $2a$)
            if (adminUser.getPassword() == null || !adminUser.getPassword().startsWith("$2a$")) {
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                changed = true;
                System.out.println("Senha do ADMIN corrigida e criptografada com sucesso.");
            }
            
            if (changed) {
                userRepository.save(adminUser);
                System.out.println("Role/Senha do usuário " + adminEmail + " atualizada.");
            }
        } else {
            User newAdmin = User.builder()
                    .username("arthuradmin")
                    .nome("Arthur Morelo")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123")) // Senha padrão
                    .role(RoleEnum.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(newAdmin);
            System.out.println("Usuário ADMIN criado: " + adminEmail + " (senha: admin123)");
        }
    }
}
