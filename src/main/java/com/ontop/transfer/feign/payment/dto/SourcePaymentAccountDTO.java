package com.ontop.transfer.feign.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SourcePaymentAccountDTO {
    private String type;
    private SourceInformationDTO sourceInformation;
    private PaymentAccountDTO account;
}
