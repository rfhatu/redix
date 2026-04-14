package com.radix.loan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateLoanRequest(
        @NotNull @Positive BigDecimal loanAmount,
        @Min(1) int term
) {
}
