package com.ontop.transfer.mapper;

import com.ontop.transfer.controller.request.MoneyTransferRequest;
import com.ontop.transfer.controller.response.MoneyTransferResponse;
import com.ontop.transfer.repository.entity.TransferTransaction;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import com.ontop.transfer.service.dto.MoneyTransferResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class MoneyTransferMapper {
    public TransferTransaction toTransferTransaction(MoneyTransferRequestDTO moneyTransferRequestDTO) {
        if ( moneyTransferRequestDTO == null ) {
            return null;
        }

        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setIdempotencyKey(moneyTransferRequestDTO.getIdempotencyKey());
        transferTransaction.setTransactionAmount(moneyTransferRequestDTO.getTransactionAmount());
        transferTransaction.setFromWalletId(moneyTransferRequestDTO.getFromWalletId());

        return transferTransaction;
    }

    public MoneyTransferRequestDTO toMoneyTransferRequestDTO(MoneyTransferRequest moneyTransferRequest) {
        if(moneyTransferRequest == null) {
            return null;
        }

        MoneyTransferRequestDTO moneyTransferRequestDTO = new MoneyTransferRequestDTO();
        moneyTransferRequestDTO.setIdempotencyKey(moneyTransferRequest.getIdempotencyKey());
        moneyTransferRequestDTO.setFromWalletId(moneyTransferRequest.getFromWalletId());
        moneyTransferRequestDTO.setTransactionAmount(moneyTransferRequest.getTransactionAmount());

        return moneyTransferRequestDTO;
    }

    public MoneyTransferResponseDTO toMoneyTransferResponseDTO(TransferTransaction transferTransaction) {
        if ( transferTransaction == null ) {
            return null;
        }

        MoneyTransferResponseDTO moneyTransferResponseDTO = new MoneyTransferResponseDTO();
        moneyTransferResponseDTO.setTransactionId(transferTransaction.getTransactionId());
        moneyTransferResponseDTO.setIdempotencyKey(transferTransaction.getIdempotencyKey());
        moneyTransferResponseDTO.setFromWalletId(transferTransaction.getFromWalletId());
        moneyTransferResponseDTO.setTransactionAmount(transferTransaction.getTransactionAmount());
        moneyTransferResponseDTO.setFeeAmount(transferTransaction.getFeeAmount());
        moneyTransferResponseDTO.setTransferredAmount(transferTransaction.getTransferredAmount());
        moneyTransferResponseDTO.setTransactionDateTime(transferTransaction.getTransactionDateTime());
        moneyTransferResponseDTO.setWalletTransactionId(transferTransaction.getWalletTransactionId());
        moneyTransferResponseDTO.setPaymentTransactionId(transferTransaction.getPaymentTransactionId());
        moneyTransferResponseDTO.setPaymentStatus(transferTransaction.getPaymentStatus());

        return moneyTransferResponseDTO;
    }

    public MoneyTransferResponse toMoneyTransferResponse(MoneyTransferResponseDTO moneyTransferResponseDTO) {
        if ( moneyTransferResponseDTO == null ) {
            return null;
        }

        MoneyTransferResponse moneyTransferResponse = new MoneyTransferResponse();
        moneyTransferResponse.setTransactionId(moneyTransferResponseDTO.getTransactionId());
        moneyTransferResponse.setIdempotencyKey(moneyTransferResponseDTO.getIdempotencyKey());
        moneyTransferResponse.setFromWalletId(moneyTransferResponseDTO.getFromWalletId());
        moneyTransferResponse.setTransactionAmount(moneyTransferResponseDTO.getTransactionAmount());
        moneyTransferResponse.setFeeAmount(moneyTransferResponseDTO.getFeeAmount());
        moneyTransferResponse.setTransferredAmount(moneyTransferResponseDTO.getTransferredAmount());
        moneyTransferResponse.setTransactionDateTime(moneyTransferResponseDTO.getTransactionDateTime());
        moneyTransferResponse.setWalletTransactionId(moneyTransferResponseDTO.getWalletTransactionId());
        moneyTransferResponse.setPaymentTransactionId(moneyTransferResponseDTO.getPaymentTransactionId());
        moneyTransferResponse.setPaymentStatus(moneyTransferResponseDTO.getPaymentStatus());

        return moneyTransferResponse;
    }
}
