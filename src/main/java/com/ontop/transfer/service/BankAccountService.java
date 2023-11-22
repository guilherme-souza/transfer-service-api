package com.ontop.transfer.service;

import com.ontop.transfer.feign.bank.dto.BankAccountDTO;

public interface BankAccountService {
    BankAccountDTO getBankAccountByWalletId(Long walletId);
}
