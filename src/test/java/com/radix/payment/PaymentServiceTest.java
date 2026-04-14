package com.radix.payment;

import com.radix.common.OverpaymentException;
import com.radix.loan.Loan;
import com.radix.loan.LoanService;
import com.radix.loan.LoanStatus;
import com.radix.loan.dto.CreateLoanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private PaymentService paymentService;

    private Loan activeLoan;
    private CreateLoanRequest validRequest;

    @BeforeEach
    void setUp() {
        activeLoan = new Loan(
       1L, new BigDecimal("1000.00"),12,
        LoanStatus.ACTIVE, new BigDecimal("1000.00"));
    }

    // =======================
    // Validation
    // =======================

    @Test
    void recordPayment_shouldThrowException_whenLoanIdIsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> paymentService.recordPayment(null, BigDecimal.TEN));

        assertEquals("loanId is required", ex.getMessage());
        verifyNoInteractions(loanService, paymentRepository);
    }

    @Test
    void recordPayment_shouldThrowException_whenAmountIsNull() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> paymentService.recordPayment(1L, null));

        assertEquals("paymentAmount must be positive", ex.getMessage());
        verifyNoInteractions(loanService, paymentRepository);
    }

    @Test
    void recordPayment_shouldThrowException_whenAmountIsZeroOrNegative() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> paymentService.recordPayment(1L, BigDecimal.ZERO));

        assertEquals("paymentAmount must be positive", ex.getMessage());
        verifyNoInteractions(loanService, paymentRepository);
    }

    // =======================
    // Business rules
    // =======================

    @Test
    void recordPayment_shouldThrowException_whenLoanIsAlreadySettled() {
        activeLoan.setStatus(LoanStatus.SETTLED);

        when(loanService.getById(1L)).thenReturn(activeLoan);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> paymentService.recordPayment(1L, BigDecimal.TEN));

        assertEquals("Loan is already settled", ex.getMessage());
        verify(loanService).getById(1L);
        verifyNoMoreInteractions(loanService);
        verifyNoInteractions(paymentRepository);
    }

    @Test
    void recordPayment_shouldThrowOverpaymentException_whenAmountExceedsBalance() {
        when(loanService.getById(1L)).thenReturn(activeLoan);

        OverpaymentException ex =
                assertThrows(OverpaymentException.class,
                        () -> paymentService.recordPayment(1L, new BigDecimal("1500")));

        assertEquals("Payment exceeds remaining balance", ex.getMessage());
        verify(loanService).getById(1L);
        verifyNoMoreInteractions(loanService);
        verifyNoInteractions(paymentRepository);
    }

    // =======================
    // Success cases
    // =======================

    @Test
    void recordPayment_shouldRecordPartialPaymentAndKeepLoanActive() {
        when(loanService.getById(1L)).thenReturn(activeLoan);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal paymentAmount = new BigDecimal("300.00");

        Payment payment = paymentService.recordPayment(1L, paymentAmount);

        assertNotNull(payment);
        assertEquals(1L, payment.getLoanId());
        assertEquals(paymentAmount, payment.getPaymentAmount());

        assertEquals(new BigDecimal("700.00"), activeLoan.getRemainingBalance());
        assertEquals(LoanStatus.ACTIVE, activeLoan.getStatus());

        verify(loanService).save(activeLoan);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void recordPayment_shouldSettleLoan_whenPaymentClearsBalance() {
        when(loanService.getById(1L)).thenReturn(activeLoan);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal paymentAmount = new BigDecimal("1000.00");

        Payment payment = paymentService.recordPayment(1L, paymentAmount);

        assertNotNull(payment);
        assertEquals(new BigDecimal("0.00"), activeLoan.getRemainingBalance());
        assertEquals(LoanStatus.SETTLED, activeLoan.getStatus());

        verify(loanService).save(activeLoan);
        verify(paymentRepository).save(any(Payment.class));
    }
}