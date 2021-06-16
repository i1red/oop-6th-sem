package app.controller;

import app.model.dto.PaymentReadDTO;
import app.model.dto.PaymentWriteDTO;
import app.model.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PaymentController {
    private PaymentService paymentService;

    @PostMapping("/")
    public PaymentReadDTO create(PaymentWriteDTO paymentWriteDTO) {
        return paymentService.create(paymentWriteDTO);
    }
}
