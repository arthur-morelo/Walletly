package com.proint.walletly.service;

import com.proint.walletly.dto.transacao.TransacaoFilterDTO;
import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;
import com.proint.walletly.dto.dashboard.ResumoMensalDTO;
import com.proint.walletly.model.Transacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
            userId, // usuarioId
            null, // contaId
            null, // categoriaId
            null, // tipoTransacao
            null, // descricao
            null, // dataInicio
            null  // dataFim
        );
        
        return transacaoService.groupByMonth(filter);
    }

    public List<ResumoMensalDTO> getResumoMensal(Long userId) {
        System.out.println("[DEBUG] Buscando resumo mensal para o usuarioId: " + userId);
        TransacaoFilterDTO filter = new TransacaoFilterDTO(userId, null, null, null, null, null, null);
        List<Transacao> transacoes = transacaoService.findWithFilters(filter);
        System.out.println("[DEBUG] Total de transacoes encontradas para o usuario: " + transacoes.size());

        // Group by year-month string like "2026-01" to keep chronological order
        Map<String, List<Transacao>> groupedByMonth = transacoes.stream()
            .collect(Collectors.groupingBy(
                t -> String.format("%04d-%02d", t.getDataTransacao().getYear(), t.getDataTransacao().getMonthValue()),
                TreeMap::new, // Ensures keys are sorted
                Collectors.toList()
            ));

        List<ResumoMensalDTO> resumoList = new ArrayList<>();
        Locale ptBR = Locale.of("pt", "BR");

        for (Map.Entry<String, List<Transacao>> entry : groupedByMonth.entrySet()) {
            List<Transacao> txList = entry.getValue();
            if (txList.isEmpty()) continue;

            BigDecimal ganhos = BigDecimal.ZERO;
            BigDecimal gastos = BigDecimal.ZERO;

            for (Transacao tx : txList) {
                String tipo = tx.getTipoTransacao() != null ? tx.getTipoTransacao().toUpperCase() : "";
                
                // Determine if it's income or expense based on common OFX types and custom ones
                boolean isReceita = tipo.equals("RECEITA") || tipo.equals("CREDIT") || 
                                    tipo.equals("DEP") || tipo.equals("INT") || 
                                    tipo.equals("DIV") || tipo.equals("DIRECTDEP");
                                    
                boolean isDespesa = tipo.equals("DESPESA") || tipo.equals("DEBIT") || 
                                    tipo.equals("PAYMENT") || tipo.equals("FEE") || 
                                    tipo.equals("SRVCHG") || tipo.equals("ATM") || 
                                    tipo.equals("POS") || tipo.equals("CHECK") || 
                                    tipo.equals("CASH") || tipo.equals("DIRECTDEBI") || 
                                    tipo.equals("REPEATPMT") || tipo.equals("OTHER");

                if (isReceita) {
                    ganhos = ganhos.add(tx.getValor());
                } else if (isDespesa) {
                    gastos = gastos.add(tx.getValor());
                } else {
                    System.out.println("[DEBUG] Transação ignorada por tipo não reconhecido: " + tx.getTipoTransacao());
                }
            }

            // Converter "2026-01" para "Janeiro/2026"
            java.time.YearMonth ym = java.time.YearMonth.parse(entry.getKey());
            String mesFormatado = ym.getMonth().getDisplayName(TextStyle.FULL, ptBR);
            // Capitalize first letter
            mesFormatado = mesFormatado.substring(0, 1).toUpperCase() + mesFormatado.substring(1) + "/" + ym.getYear();

            System.out.println("[DEBUG] Mês formatado: " + mesFormatado + " | Ganhos: " + ganhos + " | Gastos: " + gastos);
            resumoList.add(new ResumoMensalDTO(mesFormatado, ganhos, gastos));
        }

        System.out.println("[DEBUG] Retornando resumo list com tamanho: " + resumoList.size());
        return resumoList;
    }
}