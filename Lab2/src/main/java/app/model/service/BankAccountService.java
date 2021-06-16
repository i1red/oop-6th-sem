package app.model.service;

import app.model.dto.BankAccountReadDTO;
import app.model.dto.BankAccountWriteDTO;
import app.model.entity.BankAccount;
import app.model.mapper.BankAccountMapper;
import app.model.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private BankAccountMapper bankAccountMapper;

    public BankAccountReadDTO create(BankAccountWriteDTO bankAccountWriteDTO) {
        BankAccount bankAccount = bankAccountMapper.fromDTO(bankAccountWriteDTO);
        BankAccount saveAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(saveAccount);
    }

    public BankAccountReadDTO setBlocked(int id, boolean blocked) {
        return bankAccountMapper.toDTO(bankAccountRepository.save(bankAccountRepository.getOne(id).setBlocked(blocked)));
    }

    public List<BankAccountReadDTO> list() {
        return bankAccountRepository.findAll().stream().map(bankAccountMapper::toDTO).collect(Collectors.toList());
    }
}
