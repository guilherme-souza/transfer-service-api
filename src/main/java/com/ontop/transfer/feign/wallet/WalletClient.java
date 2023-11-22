package com.ontop.transfer.feign.wallet;

import com.ontop.transfer.feign.wallet.dto.WalletBalanceDTO;
import com.ontop.transfer.feign.wallet.dto.WalletTransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "wallet", url = "http://mockoon.tools.getontop.com:3000/wallets")
public interface WalletClient {

    @RequestMapping(method = RequestMethod.GET, value = "/balance?user_id={userId}")
    WalletBalanceDTO getWalletBalanceByUserId(@PathVariable("userId") Long userId);

    @RequestMapping(method = RequestMethod.POST, value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE)
    WalletTransactionDTO createWalletTransaction(@RequestBody WalletTransactionDTO walletTransactionDTO);
}
