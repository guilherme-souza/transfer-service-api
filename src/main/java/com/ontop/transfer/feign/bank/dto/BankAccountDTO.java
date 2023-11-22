package com.ontop.transfer.feign.bank.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankAccountDTO {
    private String customerName;
    private String customerDocumentNumber;
    private String accountNumber;
    private String currency;
    private String routingNumber;
}
