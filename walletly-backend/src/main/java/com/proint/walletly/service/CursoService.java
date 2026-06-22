package com.proint.walletly.service;

import com.proint.walletly.dto.curso.CursoDTO;
import com.proint.walletly.mapper.CursoMapper;
import com.proint.walletly.model.Curso;
import com.proint.walletly.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    @Transactional(readOnly = true)
    public List<CursoDTO> findAll() {
        return cursoMapper.toDTOList(cursoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CursoDTO findById(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: " + id));
        return cursoMapper.toDTO(curso);
    }

    @Transactional
    public CursoDTO save(CursoDTO cursoDTO) {
        Curso curso = cursoMapper.toEntity(cursoDTO);
        Curso savedCurso = cursoRepository.save(curso);
        return cursoMapper.toDTO(savedCurso);
    }

    @Transactional
    public CursoDTO update(Long id, CursoDTO cursoDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: " + id));
        
        curso.setTitle(cursoDTO.title());
        curso.setTextLeft(cursoDTO.textLeft());
        curso.setTextRight(cursoDTO.textRight());
        curso.setCourseDescription(cursoDTO.courseDescription());
        
        return cursoMapper.toDTO(cursoRepository.save(curso));
    }

    @Transactional
    public void delete(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com o id: " + id));
        cursoRepository.delete(curso);
    }
}
