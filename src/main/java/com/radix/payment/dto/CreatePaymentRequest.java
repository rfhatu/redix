package com.radix.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotNull Long loanId,
        @NotNull @Positive BigDecimal paymentAmount
) {
}
