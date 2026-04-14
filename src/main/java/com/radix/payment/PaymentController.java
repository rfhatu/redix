package com.radix.payment;

import com.radix.payment.dto.CreatePaymentRequest;
import com.radix.payment.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        Payment payment = paymentService.recordPayment(request.loanId(), request.paymentAmount());
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }
}
