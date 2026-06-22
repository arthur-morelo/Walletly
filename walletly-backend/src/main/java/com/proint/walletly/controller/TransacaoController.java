package com.proint.walletly.controller;

import com.proint.walletly.dto.transacao.TransacaoDTO;
import com.proint.walletly.dto.transacao.TransacaoFilterDTO;
import com.proint.walletly.dto.transacao.TransacaoGroupedDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proint.walletly.service.TransacaoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @Autowired
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<TransacaoDTO> createTransacao(@Valid @RequestBody TransacaoDTO transacao) {
        TransacaoDTO savedTransacao = transacaoService.save(transacao);
        return new ResponseEntity<>(savedTransacao, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDTO> getTransacaoById(@PathVariable Long id) {
        Optional<TransacaoDTO> transacao = transacaoService.findById(id);
        return transacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<TransacaoDTO>> getAllTransacoes(Pageable pageable) {
        Page<TransacaoDTO> transacoes = transacaoService.findAll(pageable);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TransacaoDTO>> getTransacoesWithFilters(
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String tipoTransacao,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Pageable pageable
    ) {
        TransacaoFilterDTO filter = new TransacaoFilterDTO(
                null,
                contaId,
                categoriaId,
                tipoTransacao,
                descricao,
                dataInicio,
                dataFim
        );
        Page<TransacaoDTO> transacoes = transacaoService.findWithFilters(filter, pageable);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/group/category")
    public ResponseEntity<List<TransacaoGroupedDTO>> groupByCategory(
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String tipoTransacao,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        TransacaoFilterDTO filter = new TransacaoFilterDTO(
                null,
                contaId,
                categoriaId,
                tipoTransacao,
                descricao,
                dataInicio,
                dataFim
        );
        List<TransacaoGroupedDTO> grouped = transacaoService.groupByCategory(filter);
        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/group/account")
    public ResponseEntity<List<TransacaoGroupedDTO>> groupByAccount(
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String tipoTransacao,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        TransacaoFilterDTO filter = new TransacaoFilterDTO(
                null,
                contaId,
                categoriaId,
                tipoTransacao,
                descricao,
                dataInicio,
                dataFim
        );
        List<TransacaoGroupedDTO> grouped = transacaoService.groupByAccount(filter);
        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/group/month")
    public ResponseEntity<List<TransacaoGroupedDTO>> groupByMonth(
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String tipoTransacao,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        TransacaoFilterDTO filter = new TransacaoFilterDTO(
                null,
                contaId,
                categoriaId,
                tipoTransacao,
                descricao,
                dataInicio,
                dataFim
        );
        List<TransacaoGroupedDTO> grouped = transacaoService.groupByMonth(filter);
        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/group/type")
    public ResponseEntity<List<TransacaoGroupedDTO>> groupByType(
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String tipoTransacao,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        TransacaoFilterDTO filter = new TransacaoFilterDTO(
                null,
                contaId,
                categoriaId,
                tipoTransacao,
                descricao,
                dataInicio,
                dataFim
        );
        List<TransacaoGroupedDTO> grouped = transacaoService.groupByType(filter);
        return ResponseEntity.ok(grouped);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoDTO> updateTransacao(@PathVariable Long id, @Valid @RequestBody TransacaoDTO transacaoDetails) {
        TransacaoDTO updatedTransacao = transacaoService.update(id, transacaoDetails);
        return ResponseEntity.ok(updatedTransacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransacao(@PathVariable Long id) {
        transacaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}