package com.proint.walletly.service;

import com.proint.walletly.dto.instituicao.InstituicaoDTO;
import com.proint.walletly.mapper.InstituicaoMapper;
import com.proint.walletly.model.InstituicaoFinanceira;
import com.proint.walletly.repository.InstituicaoFinanceiraRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstituicaoFinanceiraServiceTest {

    @Mock
    private InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    @Mock
    private InstituicaoMapper instituicaoMapper;

    @InjectMocks
    private InstituicaoFinanceiraService instituicaoFinanceiraService;

    private InstituicaoFinanceira testInstituicao;
    private InstituicaoDTO testInstituicaoDTO;

    @BeforeEach
    void setUp() {
        testInstituicao = InstituicaoFinanceira.builder()
                .id(1L)
                .nome("Banco Teste")
                .logoUrl("https://example.com/logo.png")
                .build();

        testInstituicaoDTO = new InstituicaoDTO(
                1L, "Banco Teste", "https://example.com/logo.png"
        );
    }

    @Test
    void save_ShouldReturnSavedInstituicao_WhenValidDTOProvided() {
        when(instituicaoMapper.toEntity(any(InstituicaoDTO.class))).thenReturn(testInstituicao);
        when(instituicaoFinanceiraRepository.save(any(InstituicaoFinanceira.class))).thenReturn(testInstituicao);
        when(instituicaoMapper.toDTO(any(InstituicaoFinanceira.class))).thenReturn(testInstituicaoDTO);

        InstituicaoDTO result = instituicaoFinanceiraService.save(testInstituicaoDTO);

        assertNotNull(result);
        assertEquals(testInstituicaoDTO.id(), result.id());
        assertEquals(testInstituicaoDTO.nome(), result.nome());
        assertEquals(testInstituicaoDTO.logoUrl(), result.logoUrl());
        verify(instituicaoFinanceiraRepository).save(any(InstituicaoFinanceira.class));
    }

    @Test
    void findById_ShouldReturnInstituicao_WhenIdExists() {
        Long id = 1L;
        when(instituicaoFinanceiraRepository.findById(id)).thenReturn(Optional.of(testInstituicao));
        when(instituicaoMapper.toDTO(testInstituicao)).thenReturn(testInstituicaoDTO);

        Optional<InstituicaoDTO> result = instituicaoFinanceiraService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(testInstituicaoDTO.id(), result.get().id());
        assertEquals(testInstituicaoDTO.nome(), result.get().nome());
        verify(instituicaoFinanceiraRepository).findById(id);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Long id = 999L;
        when(instituicaoFinanceiraRepository.findById(id)).thenReturn(Optional.empty());

        Optional<InstituicaoDTO> result = instituicaoFinanceiraService.findById(id);

        assertFalse(result.isPresent());
        verify(instituicaoFinanceiraRepository).findById(id);
    }

    @Test
    void findAll_ShouldReturnPageOfInstituicoes_WhenPageableProvided() {
        List<InstituicaoFinanceira> instituicoes = Arrays.asList(testInstituicao);
        Page<InstituicaoFinanceira> page = new PageImpl<>(instituicoes);
        Pageable pageable = PageRequest.of(0, 10);

        when(instituicaoFinanceiraRepository.findAll(pageable)).thenReturn(page);
        when(instituicaoMapper.toDTO(any(InstituicaoFinanceira.class))).thenReturn(testInstituicaoDTO);

        Page<InstituicaoDTO> result = instituicaoFinanceiraService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testInstituicaoDTO.id(), result.getContent().get(0).id());
        verify(instituicaoFinanceiraRepository).findAll(pageable);
    }

    @Test
    void update_ShouldReturnUpdatedInstituicao_WhenIdExists() {
        Long id = 1L;
        InstituicaoFinanceira updatedInstituicao = InstituicaoFinanceira.builder()
                .id(1L)
                .nome("Banco Atualizado")
                .logoUrl("https://example.com/new-logo.png")
                .build();
                
        InstituicaoDTO updatedDTO = new InstituicaoDTO(
                1L, "Banco Atualizado", "https://example.com/new-logo.png"
        );

        when(instituicaoFinanceiraRepository.findById(id)).thenReturn(Optional.of(testInstituicao));
        when(instituicaoFinanceiraRepository.save(any(InstituicaoFinanceira.class))).thenReturn(updatedInstituicao);
        when(instituicaoMapper.toDTO(any(InstituicaoFinanceira.class))).thenReturn(updatedDTO);

        InstituicaoDTO result = instituicaoFinanceiraService.update(id, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.nome(), result.nome());
        assertEquals(updatedDTO.logoUrl(), result.logoUrl());
        verify(instituicaoFinanceiraRepository).findById(id);
        verify(instituicaoFinanceiraRepository).save(any(InstituicaoFinanceira.class));
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 999L;

        when(instituicaoFinanceiraRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> instituicaoFinanceiraService.update(id, testInstituicaoDTO));
        assertEquals("Instituição financeira não encontrada com o ID " + id, exception.getMessage());
        verify(instituicaoFinanceiraRepository).findById(id);
        verify(instituicaoFinanceiraRepository, never()).save(any(InstituicaoFinanceira.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenValidIdProvided() {
        Long id = 1L;
        instituicaoFinanceiraService.deleteById(id);
        verify(instituicaoFinanceiraRepository).deleteById(id);
    }
}

