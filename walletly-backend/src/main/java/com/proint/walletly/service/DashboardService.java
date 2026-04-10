package com.proint.walletly.service;

import com.proint.walletly.dto.transacao.TransacaoFilterDTO;
import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {

    private final TransacaoService transacaoService;

    public DashboardService(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    // Renomeado para getSummary para coincidir com o Controller
    public List<TransacaoGroupedDTO> getSummary(Long userId) {
        // O Record TransacaoFilterDTO exige todos os parâmetros
        // Passamos null para os filtros que não queremos usar agora
        TransacaoFilterDTO filter = new TransacaoFilterDTO(
            null, // contaId
            null, // categoriaId
            null, // tipoTransacao
            null, // descricao
            null, // dataInicio
            null  // dataFim
        );
        
        return transacaoService.groupByMonth(filter);
    }
}