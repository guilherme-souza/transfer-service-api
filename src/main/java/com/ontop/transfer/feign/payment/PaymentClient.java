package com.ontop.transfer.feign.payment;

import com.ontop.transfer.feign.payment.dto.PaymentRequestDTO;
import com.ontop.transfer.feign.payment.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "payment", url = "http://mockoon.tools.getontop.com:3000/api/v1/payments")
public interface PaymentClient {
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    PaymentResponseDTO createPaymentTransaction(PaymentRequestDTO paymentRequestDTO);
}
