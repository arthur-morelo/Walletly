package com.proint.walletly.controller;

import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;
import com.proint.walletly.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<List<TransacaoGroupedDTO>> getDashboardSummary() {
        // Por enquanto usando um ID fixo, depois você pegará do JWT
        List<TransacaoGroupedDTO> summary = dashboardService.getSummary(1L);
        return ResponseEntity.ok(summary);
    }
}