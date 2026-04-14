package com.radix.loan;

import com.radix.loan.dto.CreateLoanRequest;
import com.radix.loan.dto.LoanResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody CreateLoanRequest request) {
        Loan loan = loanService.create(request);
        return ResponseEntity.ok(LoanMapper.toResponse(loan));
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable Long loanId) {
        Loan loan = loanService.getById(loanId);
        return ResponseEntity.ok(LoanMapper.toResponse(loan));
    }
}
