package app.controller;

import app.model.dto.CardReadDTO;
import app.model.dto.CardWriteDTO;
import app.model.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CardController {
    private CardService cardService;

    @PostMapping("/")
    public CardReadDTO createCard(CardWriteDTO cardWriteDTO) {
        return cardService.createCard(cardWriteDTO);
    }
}
