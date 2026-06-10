package com.proint.walletly.service;

import com.proint.walletly.model.Conta;
import com.proint.walletly.model.ExtratoHistory;
import com.proint.walletly.model.Transacao;
import com.proint.walletly.model.User;
import com.proint.walletly.model.enums.ExtratoStatus;
import com.proint.walletly.repository.ContaRepository;
import com.proint.walletly.repository.ExtratoHistoryRepository;
import com.proint.walletly.repository.TransacaoRepository;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.ResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.domain.data.common.TransactionList;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

@Service
public class ExtratoService {

    @Autowired
    private ExtratoHistoryRepository extratoHistoryRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Transactional
    public ExtratoHistory uploadExtrato(MultipartFile file, User user) {
        ExtratoHistory history = ExtratoHistory.builder()
                .filename(file.getOriginalFilename())
                .status(ExtratoStatus.PENDENTE)
                .user(user)
                .build();
        
        history = extratoHistoryRepository.save(history);

        try {
            List<Conta> contas = contaRepository.findByUsuario(user);
            Conta contaPadrao;
            
            if (contas.isEmpty()) {
                System.out.println("[OFX-INFO] Usuário sem contas. Criando 'Conta Padrão' automaticamente...");
                contaPadrao = Conta.builder()
                        .apelido("Conta Padrão")
                        .instituicao(null) // Pode ser nulo se não for obrigatório
                        .saldoAtual(BigDecimal.ZERO)
                        .tipoConta("CORRENTE") // Campo obrigatório em Conta
                        .usuario(user)
                        .build();
                contaPadrao = contaRepository.save(contaPadrao);
            } else {
                contaPadrao = contas.get(0);
            }

            try (InputStream is = file.getInputStream()) {
                AggregateUnmarshaller<ResponseEnvelope> unmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);
                ResponseEnvelope envelope = unmarshaller.unmarshal(is);
                
                ResponseMessageSet messageSet = envelope.getMessageSet(MessageSetType.banking);
                ResponseMessageSet creditCardMessageSet = envelope.getMessageSet(MessageSetType.creditcard);
                
                if (messageSet == null && creditCardMessageSet == null) {
                    throw new IllegalArgumentException("Arquivo OFX não contém dados bancários ou de cartão de crédito válidos.");
                }
                
                int transacoesSalvas = 0;

                // Processa conta corrente
                if (messageSet != null) {
                    BankingResponseMessageSet bankMessageSet = (BankingResponseMessageSet) messageSet;
                    List<BankStatementResponseTransaction> bankResponses = bankMessageSet.getStatementResponses();
                    if (bankResponses != null) {
                        for (BankStatementResponseTransaction response : bankResponses) {
                            BankStatementResponse statement = response.getMessage();
                            if (statement != null && statement.getTransactionList() != null && statement.getTransactionList().getTransactions() != null) {
                                transacoesSalvas += processTransactions(statement.getTransactionList().getTransactions(), contaPadrao);
                            }
                        }
                    }
                }

                // Processa cartão de crédito
                if (creditCardMessageSet != null) {
                    com.webcohesion.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet ccMessageSet = 
                        (com.webcohesion.ofx4j.domain.data.creditcard.CreditCardResponseMessageSet) creditCardMessageSet;
                    List<com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponseTransaction> ccResponses = ccMessageSet.getStatementResponses();
                    if (ccResponses != null) {
                        for (com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponseTransaction response : ccResponses) {
                            com.webcohesion.ofx4j.domain.data.creditcard.CreditCardStatementResponse statement = response.getMessage();
                            if (statement != null && statement.getTransactionList() != null && statement.getTransactionList().getTransactions() != null) {
                                transacoesSalvas += processTransactions(statement.getTransactionList().getTransactions(), contaPadrao);
                            }
                        }
                    }
                }
                
                history.setStatus(ExtratoStatus.PROCESSADO);
                history.setLogMessage("Arquivo processado com sucesso. " + transacoesSalvas + " transações geradas.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            history.setStatus(ExtratoStatus.ERRO);
            history.setLogMessage("Erro ao processar arquivo OFX: " + e.getMessage());
        }

        return extratoHistoryRepository.save(history);
    }

    private int processTransactions(List<Transaction> transactions, Conta contaPadrao) {
        int salvos = 0;
        for (Transaction ofxTx : transactions) {
            String descricao = ofxTx.getMemo();
            if (descricao == null || descricao.isEmpty()) {
                descricao = ofxTx.getName() != null ? ofxTx.getName() : "Transação OFX";
            }
            if (descricao.length() > 255) {
                descricao = descricao.substring(0, 255);
            }
            
            String tipoTransacao = ofxTx.getTransactionType().name();
            if (tipoTransacao.length() > 10) {
                tipoTransacao = tipoTransacao.substring(0, 10);
            }

            Transacao tx = Transacao.builder()
                .conta(contaPadrao)
                .descricao(descricao)
                .valor(BigDecimal.valueOf(Math.abs(ofxTx.getAmount())))
                .tipoTransacao(tipoTransacao)
                .dataTransacao(ofxTx.getDatePosted().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
            
            transacaoRepository.save(tx);
            salvos++;
        }
        return salvos;
    }

    @Transactional(readOnly = true)
    public List<ExtratoHistory> getHistory(User user) {
        return extratoHistoryRepository.findByUserOrderByUploadDateDesc(user);
    }
}
