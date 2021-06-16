package app.model.mapper;

import app.model.dto.BankAccountReadDTO;
import app.model.dto.BankAccountWriteDTO;
import app.model.entity.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface BankAccountMapper extends CommonMapper<BankAccountReadDTO, BankAccountWriteDTO, BankAccount> {
}
