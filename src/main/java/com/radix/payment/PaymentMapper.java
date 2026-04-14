package com.radix.payment;

import com.radix.payment.dto.PaymentResponse;

public final class PaymentMapper {
    private PaymentMapper() {
    }

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getLoanId(),
                payment.getPaymentAmount(),
                payment.getCreatedAt()
        );
    }
}
