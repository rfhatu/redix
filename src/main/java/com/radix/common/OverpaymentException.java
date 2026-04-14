package com.radix.common;

public class OverpaymentException extends RuntimeException {
    public OverpaymentException(String message) {
        super(message);
    }
}
