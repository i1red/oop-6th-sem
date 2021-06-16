package app.model.service;

import app.model.dto.BankAccountReadDTO;
import app.model.dto.CardReadDTO;
import app.model.dto.UserReadDTO;
import app.model.entity.BankAccount;
import app.model.entity.Card;
import app.model.mapper.BankAccountMapper;
import app.model.mapper.CardMapper;
import app.model.mapper.UserMapper;
import app.model.repository.BankAccountRepository;
import app.model.repository.CardRepository;
import app.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserService {
    private UserRepository userRepository;
    private BankAccountRepository bankAccountRepository;
    private CardRepository cardRepository;
    private UserMapper userMapper;
    private BankAccountMapper bankAccountMapper;
    private CardMapper cardMapper;

    public UserReadDTO getByUsername(String username) {
        return userMapper.toDTO(userRepository.getByUsername(username));
    }

    public List<BankAccountReadDTO> listBankAccounts(int id) {
        return bankAccountRepository.getAllByUserId(id).stream().map(bankAccountMapper::toDTO).collect(Collectors.toList());
    }

    public List<CardReadDTO> listCards(int id) {
        List<Card> cards = new ArrayList<>();
        List<BankAccount> bankAccounts = bankAccountRepository.getAllByUserId(id);

        for (BankAccount account: bankAccounts) {
            cards.addAll(cardRepository.getAllByAccountId(account.getId()));
        }

        return cards.stream().map(cardMapper::toDTO).collect(Collectors.toList());
    }

}
