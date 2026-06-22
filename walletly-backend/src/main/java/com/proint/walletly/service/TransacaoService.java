package com.proint.walletly.service;

import com.proint.walletly.dto.transacao.TransacaoDTO;
import com.proint.walletly.dto.transacao.TransacaoFilterDTO;
import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;
import com.proint.walletly.mapper.TransacaoMapper;
import com.proint.walletly.model.Transacao;
import com.proint.walletly.repository.TransacaoRepository;
import com.proint.walletly.utils.TransacaoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final TransacaoMapper transacaoMapper;

    @Autowired
    public TransacaoService(TransacaoRepository transacaoRepository, TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.transacaoMapper = transacaoMapper;
    }

    public TransacaoDTO save(TransacaoDTO dto) {
        Transacao transacao = transacaoMapper.toEntity(dto);
        Transacao saved = transacaoRepository.save(transacao);
        return transacaoMapper.toDTO(saved);
    }

    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void salvarTransacaoIsolada(Transacao transacao) {
        transacaoRepository.save(transacao);
    }

    public Optional<TransacaoDTO> findById(Long id) {
        return transacaoRepository.findById(id)
                .map(transacaoMapper::toDTO);
    }

    public Page<TransacaoDTO> findAll(Pageable pageable) {
        return transacaoRepository.findAll(pageable)
                .map(transacaoMapper::toDTO);
    }

    public Page<TransacaoDTO> findWithFilters(TransacaoFilterDTO filter, Pageable pageable) {
        Specification<Transacao> spec = TransacaoSpecification.withFilters(
                filter.contaId(),
                filter.categoriaId(),
                filter.tipoTransacao(),
                filter.descricao(),
                filter.dataInicio(),
                filter.dataFim()
        );
        return transacaoRepository.findAll(spec, pageable)
                .map(transacaoMapper::toDTO);
    }

    public List<Transacao> findWithFilters(TransacaoFilterDTO filter) {
        Specification<Transacao> spec = TransacaoSpecification.withFilters(
                filter.contaId(),
                filter.categoriaId(),
                filter.tipoTransacao(),
                filter.descricao(),
                filter.dataInicio(),
                filter.dataFim()
        );
        return transacaoRepository.findAll(spec);
    }

    public List<TransacaoGroupedDTO> groupByCategory(TransacaoFilterDTO filter) {
        List<Transacao> transacoes = findWithFilters(filter);
        return transacoes.stream()
                .filter(t -> t.getCategoria() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria().getNome(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BigDecimal total = list.stream()
                                            .map(Transacao::getValor)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    return new TransacaoGroupedDTO(
                                            list.get(0).getCategoria().getNome(),
                                            total,
                                            (long) list.size()
                                    );
                                }
                        )
                ))
                .values().stream()
                .collect(Collectors.toList());
    }

    public List<TransacaoGroupedDTO> groupByAccount(TransacaoFilterDTO filter) {
        List<Transacao> transacoes = findWithFilters(filter);
        return transacoes.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getConta().getApelido(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BigDecimal total = list.stream()
                                            .map(Transacao::getValor)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    return new TransacaoGroupedDTO(
                                            list.get(0).getConta().getApelido(),
                                            total,
                                            (long) list.size()
                                    );
                                }
                        )
                ))
                .values().stream()
                .collect(Collectors.toList());
    }

    public List<TransacaoGroupedDTO> groupByMonth(TransacaoFilterDTO filter) {
        List<Transacao> transacoes = findWithFilters(filter);
        return transacoes.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDataTransacao().getYear() + "-" + 
                             String.format("%02d", t.getDataTransacao().getMonthValue()),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BigDecimal total = list.stream()
                                            .map(Transacao::getValor)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    String monthKey = list.get(0).getDataTransacao().getYear() + "-" + 
                                                     String.format("%02d", list.get(0).getDataTransacao().getMonthValue());
                                    return new TransacaoGroupedDTO(
                                            monthKey,
                                            total,
                                            (long) list.size()
                                    );
                                }
                        )
                ))
                .values().stream()
                .sorted((a, b) -> a.groupKey().compareTo(b.groupKey()))
                .collect(Collectors.toList());
    }

    public List<TransacaoGroupedDTO> groupByType(TransacaoFilterDTO filter) {
        List<Transacao> transacoes = findWithFilters(filter);
        return transacoes.stream()
                .collect(Collectors.groupingBy(
                        Transacao::getTipoTransacao,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    BigDecimal total = list.stream()
                                            .map(Transacao::getValor)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    return new TransacaoGroupedDTO(
                                            list.get(0).getTipoTransacao(),
                                            total,
                                            (long) list.size()
                                    );
                                }
                        )
                ))
                .values().stream()
                .collect(Collectors.toList());
    }

    public TransacaoDTO update(Long id, TransacaoDTO dto) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada com o ID " + id));
        transacaoMapper.updateEntityFromDTO(dto, transacao);
        Transacao updated = transacaoRepository.save(transacao);
        return transacaoMapper.toDTO(updated);
    }

    public void deleteById(Long id) {
        transacaoRepository.deleteById(id);
    }
}
