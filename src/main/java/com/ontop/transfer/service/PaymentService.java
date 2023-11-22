package com.ontop.transfer.service;

import com.ontop.transfer.exception.NotProcessedPaymentException;
import com.ontop.transfer.feign.payment.dto.PaymentRequestDTO;
import com.ontop.transfer.feign.payment.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO payment(PaymentRequestDTO paymentRequestDTO) throws NotProcessedPaymentException;
}
