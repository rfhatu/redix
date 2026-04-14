package com.radix.loan;

import com.radix.loan.dto.LoanResponse;

public final class LoanMapper {
    private LoanMapper() {
    }

    public static LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
                loan.getLoanId(),
                loan.getLoanAmount(),
                loan.getTerm(),
                loan.getStatus(),
                loan.getRemainingBalance()
        );
    }
}
