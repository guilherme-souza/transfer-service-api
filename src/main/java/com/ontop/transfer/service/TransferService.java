package com.ontop.transfer.service;


import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import com.ontop.transfer.service.dto.MoneyTransferResponseDTO;

public interface TransferService {
    MoneyTransferResponseDTO transfer(MoneyTransferRequestDTO moneyTransferRequestDTO);
}
