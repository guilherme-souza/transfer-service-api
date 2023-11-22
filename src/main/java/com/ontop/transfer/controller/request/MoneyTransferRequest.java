package com.ontop.transfer.controller.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MoneyTransferRequest {
    private String idempotencyKey;
    @Min(value = 0, message = "A wallet identification is required.")
    private Long fromWalletId;
    @Positive(message = "A transaction amount is required.")
    private BigDecimal transactionAmount;
}
