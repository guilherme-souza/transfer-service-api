package com.ontop.transfer.mapper;

import com.ontop.transfer.configuration.OntopProperties;
import com.ontop.transfer.feign.bank.dto.BankAccountDTO;
import com.ontop.transfer.feign.payment.dto.*;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentMapper {
    public PaymentRequestDTO toPaymentRequestDTO(BigDecimal amountToTransfer, BankAccountDTO bankAccountDTO,
                                                 OntopProperties ontopProperties) {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setAmount(amountToTransfer);

        //Source
        paymentRequestDTO.setSource(toSourcePaymentAccountDTO(ontopProperties));

        //Destination
        paymentRequestDTO.setDestination(toDestinationPaymentAccountDTO(bankAccountDTO));

        return paymentRequestDTO;
    }

    private SourcePaymentAccountDTO toSourcePaymentAccountDTO(OntopProperties ontopProperties) {
        SourcePaymentAccountDTO sourcePaymentAccountDTO = new SourcePaymentAccountDTO();
        sourcePaymentAccountDTO.setType(ontopProperties.getCompanyType());
        SourceInformationDTO sourceInformationDTO = new SourceInformationDTO();
        sourceInformationDTO.setName(ontopProperties.getCompanyName());
        PaymentAccountDTO paymentAccountDTO = new PaymentAccountDTO();
        paymentAccountDTO.setAccountNumber(ontopProperties.getAccountNumber());
        paymentAccountDTO.setCurrency(ontopProperties.getCurrency());
        paymentAccountDTO.setRoutingNumber(ontopProperties.getRoutingNumber());
        sourcePaymentAccountDTO.setSourceInformation(sourceInformationDTO);
        sourcePaymentAccountDTO.setAccount(paymentAccountDTO);
        return  sourcePaymentAccountDTO;
    }

    private DestinationPaymentAccountDTO toDestinationPaymentAccountDTO(BankAccountDTO bankAccountDTO) {
        DestinationPaymentAccountDTO destinationPaymentAccountDTO = new DestinationPaymentAccountDTO();
        destinationPaymentAccountDTO.setName(bankAccountDTO.getCustomerName());
        PaymentAccountDTO paymentAccountDTO = new PaymentAccountDTO();
        paymentAccountDTO.setAccountNumber(bankAccountDTO.getAccountNumber());
        paymentAccountDTO.setCurrency(bankAccountDTO.getCurrency());
        paymentAccountDTO.setRoutingNumber(bankAccountDTO.getRoutingNumber());
        destinationPaymentAccountDTO.setAccount(paymentAccountDTO);

        return destinationPaymentAccountDTO;
    }
}
