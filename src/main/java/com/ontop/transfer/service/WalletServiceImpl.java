package com.ontop.transfer.service;

import com.ontop.transfer.exception.BusinessLogicException;
import com.ontop.transfer.feign.wallet.WalletClient;
import com.ontop.transfer.feign.wallet.dto.WalletBalanceDTO;
import com.ontop.transfer.feign.wallet.dto.WalletTransactionDTO;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletClient walletClient;

    @Autowired
    public WalletServiceImpl(WalletClient walletClient) {
        this.walletClient = walletClient;
    }

    @Override
    public WalletTransactionDTO withdraw(MoneyTransferRequestDTO moneyTransferRequestDTO) {
        //Check balance
        WalletBalanceDTO walletBalanceDTO = walletClient.getWalletBalanceByUserId(
                moneyTransferRequestDTO.getFromWalletId());

        if(moneyTransferRequestDTO.getTransactionAmount().compareTo(walletBalanceDTO.getBalance()) > 0) {
            log.error("Insufficient funds in wallet: {}", moneyTransferRequestDTO.getFromWalletId());
            throw new BusinessLogicException("Insufficient funds in wallet to complete this transfer.");
        }

        //Withdrawn from user's wallet
        WalletTransactionDTO walletTransactionDTO = walletClient.createWalletTransaction(
                createWalletTransactionDTO(moneyTransferRequestDTO));
        log.info("Withdraw from wallet, transaction id: {}, amount of: {}",
                walletTransactionDTO.getWalletTransactionId(), walletTransactionDTO.getAmount());
        return walletTransactionDTO;
    }

    private WalletTransactionDTO createWalletTransactionDTO(MoneyTransferRequestDTO moneyTransferRequestDTO) {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setUserId(moneyTransferRequestDTO.getFromWalletId());

        if(moneyTransferRequestDTO.getTransactionAmount().signum() > 0) {
            walletTransactionDTO.setAmount(moneyTransferRequestDTO.getTransactionAmount().negate());
        } else {
            walletTransactionDTO.setAmount(moneyTransferRequestDTO.getTransactionAmount());
        }

        return walletTransactionDTO;
    }
}
