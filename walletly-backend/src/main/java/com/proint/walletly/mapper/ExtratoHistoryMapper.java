package com.proint.walletly.mapper;

import com.proint.walletly.dto.ExtratoHistoryDTO;
import com.proint.walletly.model.ExtratoHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExtratoHistoryMapper {

    ExtratoHistoryMapper INSTANCE = Mappers.getMapper(ExtratoHistoryMapper.class);

    ExtratoHistoryDTO toDTO(ExtratoHistory model);
    ExtratoHistory toModel(ExtratoHistoryDTO dto);
}
