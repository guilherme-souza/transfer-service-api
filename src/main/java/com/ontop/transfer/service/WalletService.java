package com.ontop.transfer.service;

import com.ontop.transfer.feign.wallet.dto.WalletTransactionDTO;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;

public interface WalletService {
    WalletTransactionDTO withdraw(MoneyTransferRequestDTO moneyTransferRequestDTO);
}
