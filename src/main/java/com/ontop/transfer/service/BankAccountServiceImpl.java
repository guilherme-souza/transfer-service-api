package com.ontop.transfer.service;

import com.ontop.transfer.feign.bank.BankAccountClient;
import com.ontop.transfer.feign.bank.dto.BankAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankAccountServiceImpl  implements BankAccountService {
    private final BankAccountClient bankAccountClient;

    @Autowired
    public BankAccountServiceImpl(BankAccountClient bankAccountClient) {
        this.bankAccountClient = bankAccountClient;
    }

    @Override
    public BankAccountDTO getBankAccountByWalletId(Long walletId) {
        log.info("Request bank account information for wallet: {}", walletId);
        return bankAccountClient.getBankAccountByWalletId(walletId);
    }
}
