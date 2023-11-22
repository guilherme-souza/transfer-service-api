package com.ontop.transfer.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MoneyTransferResponseDTO {
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
