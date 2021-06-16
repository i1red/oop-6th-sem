package app.model.mapper;


import app.model.dto.UserReadDTO;
import app.model.dto.UserWriteDTO;
import app.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface UserMapper extends CommonMapper<UserReadDTO, UserWriteDTO, User>{
}
