package com.ontop.transfer.service;

import com.ontop.transfer.exception.NotProcessedPaymentException;
import com.ontop.transfer.feign.payment.PaymentClient;
import com.ontop.transfer.feign.payment.dto.PaymentRequestDTO;
import com.ontop.transfer.feign.payment.dto.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentClient paymentClient;

    public PaymentServiceImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Override
    public PaymentResponseDTO payment(PaymentRequestDTO paymentRequestDTO) throws NotProcessedPaymentException {
        log.info("Requesting bank transfer of: {} to: {}", paymentRequestDTO.getAmount(),
                paymentRequestDTO.getDestination().getName());

        PaymentResponseDTO paymentResponseDTO = paymentClient.createPaymentTransaction(paymentRequestDTO);
        log.info("Transfer completed with id: {}", paymentResponseDTO.getPaymentInfo().getId());
        return paymentResponseDTO;
    }
}
