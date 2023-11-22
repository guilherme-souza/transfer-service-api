package com.ontop.transfer.service;

import com.ontop.transfer.feign.bank.BankAccountClient;
import com.ontop.transfer.feign.bank.dto.BankAccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {
    @Mock
    private BankAccountClient bankAccountClient;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @Test
    public void testGetBankAccountByWalletId() {
        Long walletId = 123L;
        BankAccountDTO expectedBankAccount = getExpectedBankAccount();

        when(bankAccountClient.getBankAccountByWalletId(walletId)).thenReturn(expectedBankAccount);
        BankAccountDTO actualBankAccount = bankAccountService.getBankAccountByWalletId(walletId);

        assertEquals(expectedBankAccount, actualBankAccount);
        verify(bankAccountClient).getBankAccountByWalletId(walletId);
    }

    private BankAccountDTO getExpectedBankAccount() {
        BankAccountDTO expectedBankAccount = new BankAccountDTO();
        expectedBankAccount.setAccountNumber("123456");
        expectedBankAccount.setRoutingNumber("123456");
        expectedBankAccount.setCustomerDocumentNumber("1");
        expectedBankAccount.setCustomerName("Customer Name");
        expectedBankAccount.setCurrency("USD");
        return expectedBankAccount;
    }
}
