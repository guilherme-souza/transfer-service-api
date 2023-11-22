package com.ontop.transfer.feign.bank;

import com.ontop.transfer.feign.bank.dto.BankAccountDTO;
import feign.FeignException;
import feign.Request;
import org.springframework.stereotype.Component;

/**
 * This implementation of the request to Bank Account Service was made for development purpose.
 * It is a mock and was not implemented to facilitate running and testing Transfer Service.
 */
@Component
public class BankAccountClient {
    public BankAccountDTO getBankAccountByWalletId(Long fromWalletId) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setCustomerName("Tony Stark");
        bankAccountDTO.setCustomerDocumentNumber("12345678");
        bankAccountDTO.setCurrency("USD");
        bankAccountDTO.setAccountNumber("1885226711");
        bankAccountDTO.setRoutingNumber("211927207");
        return bankAccountDTO;
    }
}
