package com.ontop.transfer.repository;

import com.ontop.transfer.repository.entity.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, Long> {
    Optional<TransferTransaction> findByIdempotencyKey(String idempotencyKey);
}
