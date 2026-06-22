package com.proint.walletly.service;

import com.proint.walletly.dto.ExtratoHistoryDTO;
import com.proint.walletly.mapper.ExtratoHistoryMapper;
import com.proint.walletly.model.Conta;
import com.proint.walletly.model.ExtratoHistory;
import com.proint.walletly.model.Transacao;
import com.proint.walletly.model.User;
import com.proint.walletly.repository.ContaRepository;
import com.proint.walletly.repository.ExtratoHistoryRepository;
import com.proint.walletly.repository.TransacaoRepository;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponseTransaction;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtratoService {

    private final ExtratoHistoryRepository extratoHistoryRepository;
    private final ExtratoHistoryMapper extratoHistoryMapper;
    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    @Autowired
    public ExtratoService(ExtratoHistoryRepository extratoHistoryRepository,
                          ExtratoHistoryMapper extratoHistoryMapper,
                          ContaRepository contaRepository,
                          TransacaoRepository transacaoRepository) {
        this.extratoHistoryRepository = extratoHistoryRepository;
        this.extratoHistoryMapper = extratoHistoryMapper;
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public List<ExtratoHistoryDTO> getHistory(Long userId) {
        return extratoHistoryRepository.findByUsuarioIdOrderByUploadDateDesc(userId)
                .stream()
                .map(extratoHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void uploadOfx(MultipartFile file, User user) {
        ExtratoHistory history = ExtratoHistory.builder()
                .usuario(user)
                .filename(file.getOriginalFilename())
                .uploadDate(OffsetDateTime.now())
                .status("PENDENTE")
                .build();
        // Salva inicialmente para obter o ID e ter o registro.
        history = extratoHistoryRepository.save(history);

        try (InputStream is = file.getInputStream()) {
            AggregateUnmarshaller<ResponseEnvelope> unmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);
            ResponseEnvelope response = unmarshaller.unmarshal(is);
            
            Conta conta = contaRepository.findByUsuarioId(user.getId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhuma conta encontrada para o usuário. Crie uma conta antes de importar extratos."));

            int transacoesAdicionadas = 0;

            // Transações de conta corrente ou poupança
            BankingResponseMessageSet bankingResponse = (BankingResponseMessageSet) response.getMessageSet(MessageSetType.banking);
            if (bankingResponse != null && bankingResponse.getStatementResponses() != null) {
                for (BankStatementResponseTransaction statementResponse : bankingResponse.getStatementResponses()) {
                    if (statementResponse.getMessage().getTransactionList() != null) {
                        for (Transaction transaction : statementResponse.getMessage().getTransactionList().getTransactions()) {
                            salvarTransacao(transaction, conta, history);
                            transacoesAdicionadas++;
                        }
                    }
                }
            }

            // Transações de cartão de crédito
            CreditCardResponseMessageSet creditCardResponse = (CreditCardResponseMessageSet) response.getMessageSet(MessageSetType.creditcard);
            if (creditCardResponse != null && creditCardResponse.getStatementResponses() != null) {
                for (CreditCardStatementResponseTransaction statementResponse : creditCardResponse.getStatementResponses()) {
                    if (statementResponse.getMessage().getTransactionList() != null) {
                        for (Transaction transaction : statementResponse.getMessage().getTransactionList().getTransactions()) {
                            salvarTransacao(transaction, conta, history);
                            transacoesAdicionadas++;
                        }
                    }
                }
            }

            if (transacoesAdicionadas == 0) {
                throw new RuntimeException("Nenhuma transação encontrada no arquivo OFX.");
            }

            history.setStatus("PROCESSADO");
            history.setLogMessage("Arquivo processado com sucesso. " + transacoesAdicionadas + " transações importadas.");
            
            recalcularSaldo(conta);

        } catch (Exception e) {
            history.setStatus("ERRO");
            history.setLogMessage("Erro ao processar arquivo: " + e.getMessage());
        }
        extratoHistoryRepository.save(history);
    }

    private void salvarTransacao(Transaction ofxTx, Conta conta, ExtratoHistory history) {
        BigDecimal amount = BigDecimal.valueOf(ofxTx.getAmount());
        String tipo = amount.compareTo(BigDecimal.ZERO) >= 0 ? "RECEITA" : "DESPESA";
        
        BigDecimal absAmount = amount.abs();
        
        String desc = ofxTx.getMemo();
        if (desc == null || desc.trim().isEmpty()) {
            desc = ofxTx.getName();
        }
        if (desc == null || desc.trim().isEmpty()) {
            desc = "Transação Importada";
        }

        LocalDate data = ofxTx.getDatePosted() != null 
                ? ofxTx.getDatePosted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : LocalDate.now();

        Transacao transacao = Transacao.builder()
                .conta(conta)
                .descricao(desc.length() > 255 ? desc.substring(0, 255) : desc)
                .valor(absAmount)
                .tipoTransacao(tipo)
                .dataTransacao(data)
                .extratoHistory(history)
                .build();

        transacaoRepository.save(transacao);
    }
    
    private void recalcularSaldo(Conta conta) {
        List<Transacao> todas = transacaoRepository.findAllByContaId(conta.getId());
        BigDecimal novoSaldo = BigDecimal.ZERO;
        for (Transacao t : todas) {
            if ("RECEITA".equals(t.getTipoTransacao())) {
                novoSaldo = novoSaldo.add(t.getValor());
            } else {
                novoSaldo = novoSaldo.subtract(t.getValor());
            }
        }
        conta.setSaldoAtual(novoSaldo);
        conta.setDataUltimaSincronizacao(OffsetDateTime.now());
        contaRepository.save(conta);
    }

    @Transactional
    public void deleteExtrato(Long id) {
        extratoHistoryRepository.findById(id).ifPresent(history -> {
            // Conta associada a este histórico. Como as transações podem estar associadas à mesma conta:
            // Obtemos as transações associadas ao extrato para descobrir a conta
            // Wait, to recalculate balance we need to know the conta before deleting.
            Conta contaParaRecalcular = transacaoRepository.findAll().stream()
                .filter(t -> t.getExtratoHistory() != null && t.getExtratoHistory().getId().equals(id))
                .map(Transacao::getConta)
                .findFirst().orElse(null);

            extratoHistoryRepository.delete(history);
            
            // Recalcula o saldo após a exclusão (as transações vinculadas são excluídas no banco devido ao CASCADE)
            // Se as transações ainda estiverem na sessão do hibernate, precisamos garantir o flush
            transacaoRepository.flush();

            if (contaParaRecalcular != null) {
                recalcularSaldo(contaParaRecalcular);
            }
        });
    }
}
