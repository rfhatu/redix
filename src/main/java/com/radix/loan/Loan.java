package com.radix.loan;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal loanAmount;

    @Column(nullable = false)
    private int term;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal remainingBalance;

    protected Loan() {
    }

    public Loan(Long loanId, BigDecimal loanAmount, int term, LoanStatus status, BigDecimal remainingBalance) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.term = term;
        this.status = status;
        this.remainingBalance = remainingBalance;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
