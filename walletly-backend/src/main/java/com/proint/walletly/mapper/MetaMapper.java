package com.proint.walletly.mapper;

import com.proint.walletly.dto.meta.MetaDTO;
import com.proint.walletly.model.Meta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MetaMapper {
    MetaDTO toDTO(Meta meta);
    
    @Mapping(target = "user", ignore = true)
    Meta toEntity(MetaDTO metaDTO);
    
    List<MetaDTO> toDTOList(List<Meta> metas);
}
