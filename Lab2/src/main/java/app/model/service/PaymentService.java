package app.model.service;


import app.model.dto.PaymentReadDTO;
import app.model.dto.PaymentWriteDTO;
import app.model.entity.Card;
import app.model.entity.Payment;
import app.model.mapper.PaymentMapper;
import app.model.repository.CardRepository;
import app.model.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentService {
    private PaymentRepository paymentRepository;
    private CardRepository cardRepository;
    private PaymentMapper paymentMapper;

    @Transactional
    public PaymentReadDTO create(PaymentWriteDTO paymentWriteDTO) {
        Payment payment = paymentMapper.fromDTO(paymentWriteDTO);

        Card fromCard = cardRepository.findById(payment.getFromCardId()).orElseThrow(() -> new IllegalArgumentException("From card does not exist"));
        Card toCard = cardRepository.findById(payment.getToCard()).orElseThrow(() -> new IllegalArgumentException("From card does not exist"));

        if (fromCard.getBalance() < payment.getSum()) {
            throw new IllegalArgumentException("Not enough money");
        }

        cardRepository.save(fromCard.setBalance(fromCard.getBalance() - payment.getSum()));
        cardRepository.save(toCard.setBalance(toCard.getBalance() + payment.getSum()));
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }
}
