package com.proint.walletly.service;

import com.proint.walletly.dto.categoria.CategoriaDTO;
import com.proint.walletly.mapper.CategoriaMapper;
import com.proint.walletly.model.Categoria;
import com.proint.walletly.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper; // Adicionado o Mock do Mapper

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria testCategoria;
    private CategoriaDTO testCategoriaDTO;

    @BeforeEach
    void setUp() {
        testCategoria = Categoria.builder()
                .id(1L)
                .nome("Alimentação")
                .urlImagemCategoria("https://example.com/food.png")
                .build();

        // Inicializando o DTO correspondente para os testes
        testCategoriaDTO = new CategoriaDTO(1L, "Alimentação", "https://example.com/food.png");
    }

    @Test
    void save_ShouldReturnSavedCategoria_WhenValidCategoriaProvided() {
        // Configura o comportamento do Mapper e Repository
        when(categoriaMapper.toEntity(any(CategoriaDTO.class))).thenReturn(testCategoria);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(testCategoria);
        when(categoriaMapper.toDto(any(Categoria.class))).thenReturn(testCategoriaDTO);

        CategoriaDTO result = categoriaService.save(testCategoriaDTO);

        assertNotNull(result);
        assertEquals(testCategoriaDTO.getId(), result.getId());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void findById_ShouldReturnCategoria_WhenIdExists() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(testCategoria));
        when(categoriaMapper.toDto(testCategoria)).thenReturn(testCategoriaDTO);

        Optional<CategoriaDTO> result = categoriaService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testCategoriaDTO.getNome(), result.get().getNome());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<CategoriaDTO> result = categoriaService.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnPageOfCategorias_WhenPageableProvided() {
        Page<Categoria> page = new PageImpl<>(Arrays.asList(testCategoria));
        Pageable pageable = PageRequest.of(0, 10);

        when(categoriaRepository.findAll(pageable)).thenReturn(page);
        when(categoriaMapper.toDto(any(Categoria.class))).thenReturn(testCategoriaDTO);

        Page<CategoriaDTO> result = categoriaService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testCategoriaDTO.getId(), result.getContent().get(0).getId());
    }

    @Test
    void update_ShouldReturnUpdatedCategoria_WhenIdExists() {
        Long id = 1L;
        Categoria updatedCategoria = Categoria.builder().id(id).nome("Transporte").build();
        CategoriaDTO updatedDTO = new CategoriaDTO(id, "Transporte", null);

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(testCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(updatedCategoria);
        when(categoriaMapper.toDto(any(Categoria.class))).thenReturn(updatedDTO);

        CategoriaDTO result = categoriaService.update(id, updatedDTO);

        assertNotNull(result);
        assertEquals("Transporte", result.getNome());
    }

    @Test
    void update_ShouldThrowException_WhenIdDoesNotExist() {
        Long id = 999L;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoriaService.update(id, testCategoriaDTO));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenValidIdProvided() {
        categoriaService.deleteById(1L);
        verify(categoriaRepository).deleteById(1L);
    }
}