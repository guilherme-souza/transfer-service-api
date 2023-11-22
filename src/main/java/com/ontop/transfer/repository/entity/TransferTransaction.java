package com.ontop.transfer.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "TRANSFER_TRANSACTION")
public class TransferTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_TRANSACTION")
    private Long transactionId;

    @Column(name = "IDEMPOTENCY_KEY")
    private String idempotencyKey;

    @Column(name = "ID_FROM_WALLET")
    private Long fromWalletId;

    @Column(name = "DATE_TIME_TRANSACTION")
    private LocalDateTime transactionDateTime;

    @Column(name = "AMOUNT_TRANSACTION")
    private BigDecimal transactionAmount;

    @Column(name = "AMOUNT_TRANSACTION_FEE")
    private BigDecimal feeAmount;

    @Column(name = "AMOUNT_TRANSFERRED")
    private BigDecimal transferredAmount;

    @Column(name = "ID_WALLET_TRANSACTION")
    private Long walletTransactionId;

    @Column(name = "ID_PAYMENT_TRANSACTION")
    private String paymentTransactionId;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;
}
