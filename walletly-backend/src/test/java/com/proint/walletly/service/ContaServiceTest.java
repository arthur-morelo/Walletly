package com.proint.walletly.service;

import com.proint.walletly.dto.conta.ContaDTO;
import com.proint.walletly.mapper.ContaMapper;
import com.proint.walletly.model.Conta;
import com.proint.walletly.model.InstituicaoFinanceira;
import com.proint.walletly.model.User;
import com.proint.walletly.repository.ContaRepository;
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
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ContaMapper contaMapper;

    @InjectMocks
    private ContaService contaService;

    private Conta testConta;
    private ContaDTO testContaDTO;

    @BeforeEach
    void setUp() {
        testConta = Conta.builder()
                .id(1L)
                .apelido("Conta Corrente")
                .tipoConta("CORRENTE")
                .saldoAtual(new BigDecimal("1000.00"))
                .dataUltimaSincronizacao(OffsetDateTime.now())
                .build();

        testContaDTO = new ContaDTO(
                1L,
                1L,
                1L,
                "Conta Corrente",
                "CORRENTE",
                new BigDecimal("1000.00"),
                OffsetDateTime.now()
        );
    }

    @Test
    void save_ShouldReturnSavedConta_WhenValidContaProvided() {
        when(contaMapper.toEntity(any(ContaDTO.class))).thenReturn(testConta);
        when(contaRepository.save(any(Conta.class))).thenReturn(testConta);
        when(contaMapper.toDTO(any(Conta.class))).thenReturn(testContaDTO);

        ContaDTO result = contaService.save(testContaDTO);

        assertNotNull(result);
        assertEquals(testContaDTO.id(), result.id());
        assertEquals(testContaDTO.apelido(), result.apelido());
        verify(contaRepository).save(any(Conta.class));
    }

    @Test
    void findById_ShouldReturnConta_WhenIdExists() {
        Long id = 1L;
        when(contaRepository.findById(id)).thenReturn(Optional.of(testConta));
        when(contaMapper.toDTO(testConta)).thenReturn(testContaDTO);

        Optional<ContaDTO> result = contaService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(testContaDTO.id(), result.get().id());
        assertEquals(testContaDTO.apelido(), result.get().apelido());
        verify(contaRepository).findById(id);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Long id = 999L;
        when(contaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ContaDTO> result = contaService.findById(id);

        assertFalse(result.isPresent());
        verify(contaRepository).findById(id);
    }

    @Test
    void findAll_ShouldReturnPageOfContas_WhenPageableProvided() {
        List<Conta> contas = Arrays.asList(testConta);
        Page<Conta> page = new PageImpl<>(contas);
        Pageable pageable = PageRequest.of(0, 10);

        when(contaRepository.findAll(pageable)).thenReturn(page);
        when(contaMapper.toDTO(any(Conta.class))).thenReturn(testContaDTO);

        Page<ContaDTO> result = contaService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testContaDTO.id(), result.getContent().get(0).id());
        verify(contaRepository).findAll(pageable);
    }

    @Test
    void update_ShouldReturnUpdatedConta_WhenIdExists() {
        Long id = 1L;
        Conta updatedConta = Conta.builder()
                .id(1L)
                .apelido("Conta Poupança Atualizada")
                .tipoConta("POUPANCA")
                .saldoAtual(new BigDecimal("2000.00"))
                .build();
        
        ContaDTO updatedDTO = new ContaDTO(
                1L, 1L, 1L, "Conta Poupança Atualizada", "POUPANCA", new BigDecimal("2000.00"), null
        );

        when(contaRepository.findById(id)).thenReturn(Optional.of(testConta));
        when(contaRepository.save(any(Conta.class))).thenReturn(updatedConta);
        when(contaMapper.toDTO(any(Conta.class))).thenReturn(updatedDTO);

        ContaDTO result = contaService.update(id, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.apelido(), result.apelido());
        assertEquals(updatedDTO.tipoConta(), result.tipoConta());
        verify(contaRepository).findById(id);
        verify(contaRepository).save(any(Conta.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 999L;
        when(contaRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> contaService.update(id, testContaDTO));
        assertEquals("Conta não encontrada com o ID " + id, exception.getMessage());
        verify(contaRepository).findById(id);
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenValidIdProvided() {
        Long id = 1L;
        contaService.deleteById(id);
        verify(contaRepository).deleteById(id);
    }
}

