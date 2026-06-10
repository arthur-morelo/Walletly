package com.proint.walletly.service;

import com.proint.walletly.dto.orcamento.OrcamentoDTO;
import com.proint.walletly.mapper.OrcamentoMapper;
import com.proint.walletly.model.*;
import com.proint.walletly.repository.OrcamentoRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @Mock
    private OrcamentoMapper orcamentoMapper;

    @InjectMocks
    private OrcamentoService orcamentoService;

    private Orcamento testOrcamento;
    private OrcamentoDTO testOrcamentoDTO;

    @BeforeEach
    void setUp() {
        testOrcamento = Orcamento.builder()
                .id(1L)
                .valorMaximo(new BigDecimal("500.00"))
                .mes(1)
                .ano(2024)
                .build();

        testOrcamentoDTO = new OrcamentoDTO(
                1L, 1L, 1L, new BigDecimal("500.00"), 1, 2024
        );
    }

    @Test
    void save_ShouldReturnSavedOrcamento_WhenValidDTOProvided() {
        when(orcamentoMapper.toEntity(any(OrcamentoDTO.class))).thenReturn(testOrcamento);
        when(orcamentoRepository.save(any(Orcamento.class))).thenReturn(testOrcamento);
        when(orcamentoMapper.toDTO(any(Orcamento.class))).thenReturn(testOrcamentoDTO);

        OrcamentoDTO result = orcamentoService.save(testOrcamentoDTO);

        assertNotNull(result);
        assertEquals(testOrcamentoDTO.id(), result.id());
        assertEquals(testOrcamentoDTO.valorMaximo(), result.valorMaximo());
        assertEquals(testOrcamentoDTO.mes(), result.mes());
        assertEquals(testOrcamentoDTO.ano(), result.ano());
        verify(orcamentoRepository).save(any(Orcamento.class));
    }

    @Test
    void findById_ShouldReturnOrcamento_WhenIdExists() {
        Long id = 1L;
        when(orcamentoRepository.findById(id)).thenReturn(Optional.of(testOrcamento));
        when(orcamentoMapper.toDTO(testOrcamento)).thenReturn(testOrcamentoDTO);

        Optional<OrcamentoDTO> result = orcamentoService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(testOrcamentoDTO.id(), result.get().id());
        assertEquals(testOrcamentoDTO.valorMaximo(), result.get().valorMaximo());
        verify(orcamentoRepository).findById(id);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Long id = 999L;
        when(orcamentoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<OrcamentoDTO> result = orcamentoService.findById(id);

        assertFalse(result.isPresent());
        verify(orcamentoRepository).findById(id);
    }

    @Test
    void findAll_ShouldReturnPageOfOrcamentos_WhenPageableProvided() {
        List<Orcamento> orcamentos = Arrays.asList(testOrcamento);
        Page<Orcamento> page = new PageImpl<>(orcamentos);
        Pageable pageable = PageRequest.of(0, 10);

        when(orcamentoRepository.findAll(pageable)).thenReturn(page);
        when(orcamentoMapper.toDTO(any(Orcamento.class))).thenReturn(testOrcamentoDTO);

        Page<OrcamentoDTO> result = orcamentoService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testOrcamentoDTO.id(), result.getContent().get(0).id());
        verify(orcamentoRepository).findAll(pageable);
    }

    @Test
    void update_ShouldReturnUpdatedOrcamento_WhenIdExists() {
        Long id = 1L;
        Orcamento updatedOrcamento = Orcamento.builder()
                .id(1L)
                .valorMaximo(new BigDecimal("750.00"))
                .mes(2)
                .ano(2024)
                .build();
                
        OrcamentoDTO updatedDTO = new OrcamentoDTO(
                1L, 1L, 1L, new BigDecimal("750.00"), 2, 2024
        );

        when(orcamentoRepository.findById(id)).thenReturn(Optional.of(testOrcamento));
        when(orcamentoRepository.save(any(Orcamento.class))).thenReturn(updatedOrcamento);
        when(orcamentoMapper.toDTO(any(Orcamento.class))).thenReturn(updatedDTO);

        OrcamentoDTO result = orcamentoService.update(id, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.valorMaximo(), result.valorMaximo());
        assertEquals(updatedDTO.mes(), result.mes());
        assertEquals(updatedDTO.ano(), result.ano());
        verify(orcamentoRepository).findById(id);
        verify(orcamentoRepository).save(any(Orcamento.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 999L;

        when(orcamentoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orcamentoService.update(id, testOrcamentoDTO));
        assertEquals("Orçamento não encontrado com o ID " + id, exception.getMessage());
        verify(orcamentoRepository).findById(id);
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenValidIdProvided() {
        Long id = 1L;
        orcamentoService.deleteById(id);
        verify(orcamentoRepository).deleteById(id);
    }
}

