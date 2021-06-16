package app.model.mapper;

import app.model.dto.CardReadDTO;
import app.model.dto.CardWriteDTO;
import app.model.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface CardMapper extends CommonMapper<CardReadDTO, CardWriteDTO, Card>{
}
