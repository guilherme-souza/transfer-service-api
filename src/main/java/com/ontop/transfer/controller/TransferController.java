package com.ontop.transfer.controller;

import com.ontop.transfer.controller.request.MoneyTransferRequest;
import com.ontop.transfer.controller.response.MoneyTransferResponse;
import com.ontop.transfer.mapper.MoneyTransferMapper;
import com.ontop.transfer.service.TransferService;
import com.ontop.transfer.service.dto.MoneyTransferResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/money-transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private MoneyTransferMapper moneyTransferMapper;

    @PostMapping
    public ResponseEntity<MoneyTransferResponse> moneyTransfer(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody @Validated MoneyTransferRequest moneyTransferRequest) {

        log.info("Initiating transfer with key: {}", idempotencyKey);
        moneyTransferRequest.setIdempotencyKey(idempotencyKey);

        MoneyTransferResponseDTO responseDTO =
                transferService.transfer(moneyTransferMapper.toMoneyTransferRequestDTO(moneyTransferRequest));

        return new ResponseEntity<>(moneyTransferMapper.toMoneyTransferResponse(responseDTO),
                    HttpStatus.CREATED);
    }
}
