package com.ontop.transfer.service;

import com.ontop.transfer.exception.NotProcessedPaymentException;
import com.ontop.transfer.feign.payment.PaymentClient;
import com.ontop.transfer.feign.payment.dto.DestinationPaymentAccountDTO;
import com.ontop.transfer.feign.payment.dto.PaymentInfoDTO;
import com.ontop.transfer.feign.payment.dto.PaymentRequestDTO;
import com.ontop.transfer.feign.payment.dto.PaymentResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void testPaymentSuccessful() throws NotProcessedPaymentException {
        PaymentRequestDTO requestDTO = mockPaymentRequest();
        PaymentResponseDTO responseDTO = mockPaymentResponse();
        when(paymentClient.createPaymentTransaction(any(PaymentRequestDTO.class))).thenReturn(responseDTO);

        PaymentResponseDTO result = paymentService.payment(requestDTO);

        verify(paymentClient).createPaymentTransaction(requestDTO);
        assertEquals(responseDTO, result);
    }

    @Test
    public void testPaymentNotProcessed() {
        PaymentRequestDTO requestDTO = mockPaymentRequest();
        when(paymentClient.createPaymentTransaction(any())).thenThrow(NotProcessedPaymentException.class);

        assertThrows(NotProcessedPaymentException.class, () -> paymentService.payment(requestDTO));

        verify(paymentClient).createPaymentTransaction(requestDTO);
    }

    private PaymentRequestDTO mockPaymentRequest() {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setAmount(BigDecimal.TEN);

        DestinationPaymentAccountDTO destinationPaymentAccountDTO = new DestinationPaymentAccountDTO();
        destinationPaymentAccountDTO.setName("Testing Name");

        paymentRequestDTO.setDestination(destinationPaymentAccountDTO);
        return paymentRequestDTO;
    }

    private PaymentResponseDTO mockPaymentResponse() {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
        paymentInfoDTO.setId("123");
        paymentResponseDTO.setPaymentInfo(paymentInfoDTO);
        return paymentResponseDTO;
    }
}

