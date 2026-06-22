package com.proint.walletly.controller;

import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;
import com.proint.walletly.dto.dashboard.ResumoMensalDTO;
import com.proint.walletly.model.User;
import com.proint.walletly.repository.UserRepository;
import com.proint.walletly.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    public DashboardController(DashboardService dashboardService, UserRepository userRepository) {
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).body("Não autenticado");
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<TransacaoGroupedDTO> summary = dashboardService.getSummary(user.getId());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/resumo-mensal")
    public ResponseEntity<?> getResumoMensal(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).body("Não autenticado");
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        List<ResumoMensalDTO> resumo = dashboardService.getResumoMensal(user.getId());
        return ResponseEntity.ok(resumo);
    }
}