package com.radix.loan.dto;

import com.radix.loan.LoanStatus;

import java.math.BigDecimal;

public record LoanResponse(
        Long loanId,
        BigDecimal loanAmount,
        int term,
        LoanStatus status,
        BigDecimal remainingBalance
) {
}
