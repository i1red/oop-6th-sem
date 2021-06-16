package app.controller;

import app.model.dto.BankAccountReadDTO;
import app.model.dto.CardReadDTO;
import app.model.dto.UserReadDTO;
import app.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserController {
    private UserService userService;

    @GetMapping("/")
    public UserReadDTO getByUsername(@RequestParam String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/{id}/bank-accounts")
    public List<BankAccountReadDTO> listBankAccounts(@PathVariable Integer id) {
        return userService.listBankAccounts(id);
    }

    @GetMapping("/{id}/cards")
    private List<CardReadDTO> listCards(@PathVariable Integer id) {
        return userService.listCards(id);
    }
}
