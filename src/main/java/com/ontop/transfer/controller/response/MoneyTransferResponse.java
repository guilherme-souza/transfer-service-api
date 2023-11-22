package com.ontop.transfer.controller.response;

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
public class MoneyTransferResponse {
    private Long transactionId;
    private String idempotencyKey;
    private Long fromWalletId;
    private BigDecimal transactionAmount;
    private BigDecimal feeAmount;
    private BigDecimal transferredAmount;
    private LocalDateTime transactionDateTime;
    private Long walletTransactionId;
    private String paymentTransactionId;
    private String paymentStatus;
}
