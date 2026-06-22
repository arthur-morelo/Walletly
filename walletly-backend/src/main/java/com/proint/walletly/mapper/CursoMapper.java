package com.proint.walletly.mapper;

import com.proint.walletly.dto.curso.CursoDTO;
import com.proint.walletly.model.Curso;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CursoMapper {
    CursoDTO toDTO(Curso curso);
    Curso toEntity(CursoDTO cursoDTO);
    List<CursoDTO> toDTOList(List<Curso> cursos);
}
