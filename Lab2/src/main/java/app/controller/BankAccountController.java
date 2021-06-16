package app.controller;

import app.model.dto.BankAccountReadDTO;
import app.model.dto.BankAccountWriteDTO;
import app.model.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@RestController
@RequestMapping("/bank-accounts")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PostMapping
    @ResponseBody
    public BankAccountReadDTO create(@RequestBody BankAccountWriteDTO writeDTO) {
        return bankAccountService.create(writeDTO);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    @RolesAllowed("admin")
    public BankAccountReadDTO setBlocked(@PathVariable Integer id, @RequestParam Boolean blocked) {
        return bankAccountService.setBlocked(id, blocked);
    }

    @GetMapping
    @ResponseBody
    @RolesAllowed("admin")
    public List<BankAccountReadDTO> list() {
        return bankAccountService.list();
    }

}
