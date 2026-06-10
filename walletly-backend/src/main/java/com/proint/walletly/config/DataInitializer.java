package com.proint.walletly.config;

import com.proint.walletly.model.User;
import com.proint.walletly.model.enums.RoleEnum;
import com.proint.walletly.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        if (!userRepository.existsByEmail("arthurmorelo@gmail.com")) {
            User admin = new User();
            admin.setUsername("arthuradmin"); // Campo NOT NULL e UNIQUE na sua tabela
            admin.setNome("Arthur Morelo");
            admin.setEmail("arthurmorelo@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(RoleEnum.ADMIN);
            admin.setIsActive(true);
            
            userRepository.save(admin);
            System.out.println("====== USUÁRIO ADMIN CRIADO COM SUCESSO ======");
        }
    }
}
