package app.model.service;


import app.model.dto.CardReadDTO;
import app.model.dto.CardWriteDTO;
import app.model.mapper.CardMapper;
import app.model.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CardService {
    private CardRepository cardRepository;
    private CardMapper cardMapper;

    public CardReadDTO createCard(CardWriteDTO cardWriteDTO) {
        return cardMapper.toDTO(cardRepository.save(cardMapper.fromDTO(cardWriteDTO)));
    }
}
