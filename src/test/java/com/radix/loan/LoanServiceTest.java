package com.radix.loan;

import com.radix.common.NotFoundException;
import com.radix.loan.dto.CreateLoanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    private CreateLoanRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateLoanRequest(
                new BigDecimal("1000.00"),
                12
        );
    }

    // =======================
    // create()
    // =======================

    @Test
    void create_shouldCreateLoan_whenRequestIsValid() {
        Loan savedLoan = new Loan();
        savedLoan.setLoanAmount(validRequest.loanAmount());
        savedLoan.setTerm(validRequest.term());
        savedLoan.setStatus(LoanStatus.ACTIVE);
        savedLoan.setRemainingBalance(validRequest.loanAmount());

        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan result = loanService.create(validRequest);

        assertNotNull(result);
        assertEquals(validRequest.loanAmount(), result.getLoanAmount());
        assertEquals(validRequest.term(), result.getTerm());
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
        assertEquals(validRequest.loanAmount(), result.getRemainingBalance());

        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void create_shouldThrowException_whenLoanAmountIsNull() {
        CreateLoanRequest req = new CreateLoanRequest(null, 12);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> loanService.create(req));

        assertEquals("loanAmount must be positive", ex.getMessage());
        verifyNoInteractions(loanRepository);
    }

    @Test
    void create_shouldThrowException_whenLoanAmountIsZeroOrNegative() {
        CreateLoanRequest req = new CreateLoanRequest(BigDecimal.ZERO, 12);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> loanService.create(req));

        assertEquals("loanAmount must be positive", ex.getMessage());
        verifyNoInteractions(loanRepository);
    }

    @Test
    void create_shouldThrowException_whenTermIsZeroOrNegative() {
        CreateLoanRequest req = new CreateLoanRequest(new BigDecimal("1000"), 0);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> loanService.create(req));

        assertEquals("term must be >= 1", ex.getMessage());
        verifyNoInteractions(loanRepository);
    }

    // =======================
    // getById()
    // =======================

    @Test
    void getById_shouldReturnLoan_whenLoanExists() {
        Long loanId = 1L;
        Loan loan = new Loan();
        loan.setLoanId(loanId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        Loan result = loanService.getById(loanId);

        assertNotNull(result);
        assertEquals(loanId, result.getLoanId());
        verify(loanRepository).findById(loanId);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenLoanDoesNotExist() {
        Long loanId = 99L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        NotFoundException ex =
                assertThrows(NotFoundException.class, () -> loanService.getById(loanId));

        assertEquals("Loan not found: " + loanId, ex.getMessage());
        verify(loanRepository).findById(loanId);
    }

    // =======================
    // save()
    // =======================

    @Test
    void save_shouldDelegateToRepository() {
        Loan loan = new Loan();

        when(loanRepository.save(loan)).thenReturn(loan);

        Loan result = loanService.save(loan);

        assertNotNull(result);
        verify(loanRepository, times(1)).save(loan);
    }
}