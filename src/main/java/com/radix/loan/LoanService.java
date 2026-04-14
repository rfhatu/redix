package com.radix.loan;

import com.radix.common.NotFoundException;
import com.radix.loan.dto.CreateLoanRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan create(CreateLoanRequest req) {
        if (req.loanAmount() == null || req.loanAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("loanAmount must be positive");
        }
        if (req.term() <= 0) {
            throw new IllegalArgumentException("term must be >= 1");
        }

        Loan loan = new Loan();
        loan.setLoanAmount(req.loanAmount());
        loan.setTerm(req.term());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setRemainingBalance(req.loanAmount());

        return loanRepository.save(loan);
    }

    public Loan getById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found: " + id));
    }

    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }
}
