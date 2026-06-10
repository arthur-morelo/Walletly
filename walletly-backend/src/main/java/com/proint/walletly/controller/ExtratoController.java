package com.proint.walletly.controller;

import com.proint.walletly.model.ExtratoHistory;
import com.proint.walletly.model.User;
import com.proint.walletly.service.ExtratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/extratos")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ExtratoController {

    @Autowired
    private ExtratoService extratoService;

    @Autowired
    private com.proint.walletly.repository.UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExtrato(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        System.out.println("Recebido POST /extratos/upload - Arquivo: " + (file != null ? file.getOriginalFilename() : "NULL") + " - User: " + (userDetails != null ? userDetails.getUsername() : "NULL"));
        
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".ofx")) {
            return ResponseEntity.badRequest().body("Arquivo inválido. Por favor envie um arquivo com extensão .ofx.");
        }

        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).body("Usuário não autenticado.");
            }
            
            // Re-busca o usuário do banco para garantir que é uma entidade gerenciada (attached) pelo Hibernate
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco de dados com o username: " + userDetails.getUsername()));

            ExtratoHistory result = extratoService.uploadExtrato(file, user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO NO OFX:");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao processar upload: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(401).body("Usuário não autenticado.");
            }
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userDetails.getUsername()));
                
            List<ExtratoHistory> history = extratoService.getHistory(user);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO NO HISTÓRICO DE EXTRATOS:");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao carregar histórico: " + e.getMessage());
        }
    }
}
