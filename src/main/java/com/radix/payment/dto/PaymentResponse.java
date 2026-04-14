package com.radix.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        Long paymentId,
        Long loanId,
        BigDecimal paymentAmount,
        Instant createdAt
) {
}
