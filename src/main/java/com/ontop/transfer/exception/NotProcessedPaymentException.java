package com.ontop.transfer.exception;

public class NotProcessedPaymentException extends RuntimeException {
    public NotProcessedPaymentException(String message) {
        super(message);
    }
}
