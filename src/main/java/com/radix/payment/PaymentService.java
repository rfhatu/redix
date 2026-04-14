package com.radix.payment;

import com.radix.common.OverpaymentException;
import com.radix.loan.Loan;
import com.radix.loan.LoanService;
import com.radix.loan.LoanStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanService loanService;

    public PaymentService(PaymentRepository paymentRepository, LoanService loanService) {
        this.paymentRepository = paymentRepository;
        this.loanService = loanService;
    }

    @Transactional
    public Payment recordPayment(Long loanId, BigDecimal amount) {
        if (loanId == null) throw new IllegalArgumentException("loanId is required");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("paymentAmount must be positive");
        }

        Loan loan = loanService.getById(loanId);

        if (loan.getStatus() == LoanStatus.SETTLED) {
            throw new IllegalArgumentException("Loan is already settled");
        }

        BigDecimal remaining = loan.getRemainingBalance();
        if (amount.compareTo(remaining) > 0) {
            throw new OverpaymentException("Payment exceeds remaining balance");
        }

        BigDecimal newBalance = remaining.subtract(amount);
        loan.setRemainingBalance(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.SETTLED);
        }

        loanService.save(loan);

        Payment payment = new Payment();
        payment.setLoanId(loanId);
        payment.setPaymentAmount(amount);
        return paymentRepository.save(payment);
    }
}
