package app.model.mapper;

import app.model.dto.PaymentReadDTO;
import app.model.dto.PaymentWriteDTO;
import app.model.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapper.class)
public interface PaymentMapper extends CommonMapper<PaymentReadDTO, PaymentWriteDTO, Payment> {
}
