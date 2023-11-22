package com.ontop.transfer.feign.payment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class PaymentInfoDTO {
    private BigDecimal amount;
    private String id;
}
