package com.proint.walletly.controller;

import com.proint.walletly.dto.ExtratoHistoryDTO;
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
public class ExtratoController {

    private final ExtratoService extratoService;

    @Autowired
    public ExtratoController(ExtratoService extratoService) {
        this.extratoService = extratoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadOfx(@RequestParam("file") MultipartFile file,
                                            @AuthenticationPrincipal User user) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio.");
        }
        
        try {
            extratoService.uploadOfx(file, user);
            return ResponseEntity.ok("Upload iniciado e processado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ExtratoHistoryDTO>> getHistory(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(extratoService.getHistory(user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtrato(@PathVariable Long id) {
        extratoService.deleteExtrato(id);
        return ResponseEntity.noContent().build();
    }
}
