package com.proint.walletly.service;

import com.proint.walletly.dto.transacao.TransacaoDTO;
import com.proint.walletly.mapper.TransacaoMapper;
import com.proint.walletly.model.*;
import com.proint.walletly.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private TransacaoMapper transacaoMapper;

    @InjectMocks
    private TransacaoService transacaoService;

    private Transacao testTransacao;
    private TransacaoDTO testTransacaoDTO;

    @BeforeEach
    void setUp() {
        testTransacao = Transacao.builder()
                .id(1L)
                .descricao("Compra no supermercado")
                .valor(new BigDecimal("50.00"))
                .tipoTransacao("DESPESA")
                .dataTransacao(LocalDate.now())
                .build();

        testTransacaoDTO = new TransacaoDTO(
                1L, 1L, 1L, "Compra no supermercado", new BigDecimal("50.00"), "DESPESA", LocalDate.now()
        );
    }

    @Test
    void save_ShouldReturnSavedTransacao_WhenValidDTOProvided() {
        when(transacaoMapper.toEntity(any(TransacaoDTO.class))).thenReturn(testTransacao);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(testTransacao);
        when(transacaoMapper.toDTO(any(Transacao.class))).thenReturn(testTransacaoDTO);

        TransacaoDTO result = transacaoService.save(testTransacaoDTO);

        assertNotNull(result);
        assertEquals(testTransacaoDTO.id(), result.id());
        assertEquals(testTransacaoDTO.descricao(), result.descricao());
        assertEquals(testTransacaoDTO.valor(), result.valor());
        assertEquals(testTransacaoDTO.tipoTransacao(), result.tipoTransacao());
        assertEquals(testTransacaoDTO.dataTransacao(), result.dataTransacao());
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void findById_ShouldReturnTransacao_WhenIdExists() {
        Long id = 1L;
        when(transacaoRepository.findById(id)).thenReturn(Optional.of(testTransacao));
        when(transacaoMapper.toDTO(testTransacao)).thenReturn(testTransacaoDTO);

        Optional<TransacaoDTO> result = transacaoService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(testTransacaoDTO.id(), result.get().id());
        assertEquals(testTransacaoDTO.descricao(), result.get().descricao());
        verify(transacaoRepository).findById(id);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Long id = 999L;
        when(transacaoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<TransacaoDTO> result = transacaoService.findById(id);

        assertFalse(result.isPresent());
        verify(transacaoRepository).findById(id);
    }

    @Test
    void findAll_ShouldReturnPageOfTransacoes_WhenPageableProvided() {
        List<Transacao> transacoes = Arrays.asList(testTransacao);
        Page<Transacao> page = new PageImpl<>(transacoes);
        Pageable pageable = PageRequest.of(0, 10);

        when(transacaoRepository.findAll(pageable)).thenReturn(page);
        when(transacaoMapper.toDTO(any(Transacao.class))).thenReturn(testTransacaoDTO);

        Page<TransacaoDTO> result = transacaoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testTransacaoDTO.id(), result.getContent().get(0).id());
        verify(transacaoRepository).findAll(pageable);
    }

    @Test
    void update_ShouldReturnUpdatedTransacao_WhenIdExists() {
        Long id = 1L;
        Transacao updatedTransacao = Transacao.builder()
                .id(1L)
                .descricao("Compra atualizada")
                .valor(new BigDecimal("75.00"))
                .tipoTransacao("RECEITA")
                .dataTransacao(LocalDate.now().plusDays(1))
                .build();
                
        TransacaoDTO updatedDTO = new TransacaoDTO(
                1L, 1L, 1L, "Compra atualizada", new BigDecimal("75.00"), "RECEITA", LocalDate.now().plusDays(1)
        );

        when(transacaoRepository.findById(id)).thenReturn(Optional.of(testTransacao));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(updatedTransacao);
        when(transacaoMapper.toDTO(any(Transacao.class))).thenReturn(updatedDTO);

        TransacaoDTO result = transacaoService.update(id, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.descricao(), result.descricao());
        assertEquals(updatedDTO.valor(), result.valor());
        assertEquals(updatedDTO.tipoTransacao(), result.tipoTransacao());
        assertEquals(updatedDTO.dataTransacao(), result.dataTransacao());
        verify(transacaoRepository).findById(id);
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 999L;

        when(transacaoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transacaoService.update(id, testTransacaoDTO));
        assertEquals("Transação não encontrada com o ID " + id, exception.getMessage());
        verify(transacaoRepository).findById(id);
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenValidIdProvided() {
        Long id = 1L;
        transacaoService.deleteById(id);
        verify(transacaoRepository).deleteById(id);
    }
}

