package com.proint.walletly.mapper;

import com.proint.walletly.dto.meta.MetaDTO;
import com.proint.walletly.model.Meta;
import com.proint.walletly.model.User;
import com.proint.walletly.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MetaMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "user", expression = "java(getUser(dto.usuarioId()))")
    @Mapping(target = "id", ignore = true)
    public abstract Meta toEntity(MetaDTO dto);

    @Mapping(target = "usuarioId", source = "user.id")
    public abstract MetaDTO toDTO(Meta entity);

    public abstract List<MetaDTO> toDTOList(List<Meta> entities);

    @Mapping(target = "user", expression = "java(getUser(dto.usuarioId()))")
    @Mapping(target = "id", ignore = true)
    public abstract void updateEntityFromDTO(MetaDTO dto, @MappingTarget Meta entity);

    protected User getUser(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        return userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID " + usuarioId));
    }
}
