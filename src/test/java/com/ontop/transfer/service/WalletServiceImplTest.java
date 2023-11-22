package com.ontop.transfer.service;

import com.ontop.transfer.exception.BusinessLogicException;
import com.ontop.transfer.feign.wallet.WalletClient;
import com.ontop.transfer.feign.wallet.dto.WalletBalanceDTO;
import com.ontop.transfer.feign.wallet.dto.WalletTransactionDTO;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    @Mock
    private WalletClient walletClient;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    public void testWithdrawSufficientFunds() {
        MoneyTransferRequestDTO requestDTO = mockMoneyTransferRequest(BigDecimal.TEN);
        WalletBalanceDTO balanceDTO = mockWalletBalance();
        when(walletClient.getWalletBalanceByUserId(1L)).thenReturn(balanceDTO);
        when(walletClient.createWalletTransaction(any(WalletTransactionDTO.class)))
                .thenReturn(mockWalletTransaction());

        WalletTransactionDTO result = walletService.withdraw(requestDTO);

        verify(walletClient).createWalletTransaction(any());
        verify(walletClient).getWalletBalanceByUserId(1L);
        Assertions.assertEquals(BigDecimal.TEN, result.getAmount());
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        MoneyTransferRequestDTO requestDTO = mockMoneyTransferRequest(new BigDecimal(2000));
        WalletBalanceDTO balanceDTO = mockWalletBalance();
        when(walletClient.getWalletBalanceByUserId(1L)).thenReturn(balanceDTO);

        assertThrows(BusinessLogicException.class, () -> walletService.withdraw(requestDTO));

        verify(walletClient, never()).createWalletTransaction(any());
    }

    private MoneyTransferRequestDTO mockMoneyTransferRequest(BigDecimal amount) {
        MoneyTransferRequestDTO moneyTransferRequestDTO = new MoneyTransferRequestDTO();
        moneyTransferRequestDTO.setIdempotencyKey("Testing Key");
        moneyTransferRequestDTO.setFromWalletId(1L);
        moneyTransferRequestDTO.setTransactionAmount(amount);
        return moneyTransferRequestDTO;
    }

    private WalletBalanceDTO mockWalletBalance() {
        WalletBalanceDTO walletBalanceDTO = new WalletBalanceDTO();
        walletBalanceDTO.setUserId(1L);
        walletBalanceDTO.setBalance(new BigDecimal("1000"));
        return walletBalanceDTO;
    }

    private WalletTransactionDTO mockWalletTransaction() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setUserId(1L);
        walletTransactionDTO.setWalletTransactionId(1L);
        walletTransactionDTO.setAmount(BigDecimal.TEN);
        return walletTransactionDTO;
    }
}
