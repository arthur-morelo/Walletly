package com.proint.walletly.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;
import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;

public record DashboardSummaryDTO(
    BigDecimal saldoTotal,
    BigDecimal receitasMes,
    BigDecimal despesasMes,
    List<TransacaoGroupedDTO> gastosPorCategoria,
    List<TransacaoGroupedDTO> evolucaoMensal
) {}