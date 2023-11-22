package com.ontop.transfer.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MoneyTransferRequestDTO {
    private String idempotencyKey;
    @Min(value = 0, message = "A wallet identification is required.")
    private Long fromWalletId;
    @Positive(message = "A transaction amount is required.")
    private BigDecimal transactionAmount;
}
