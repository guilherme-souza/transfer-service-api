package com.ontop.transfer.service;

import com.ontop.transfer.configuration.OntopProperties;
import com.ontop.transfer.exception.NotProcessedPaymentException;
import com.ontop.transfer.feign.bank.dto.BankAccountDTO;
import com.ontop.transfer.feign.payment.dto.PaymentRequestDTO;
import com.ontop.transfer.feign.payment.dto.PaymentResponseDTO;
import com.ontop.transfer.feign.wallet.dto.WalletTransactionDTO;
import com.ontop.transfer.mapper.MoneyTransferMapper;
import com.ontop.transfer.mapper.PaymentMapper;
import com.ontop.transfer.repository.TransferTransactionRepository;
import com.ontop.transfer.repository.entity.TransferTransaction;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import com.ontop.transfer.service.dto.MoneyTransferResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class TransferServiceImpl implements TransferService {

    private final TransferTransactionRepository transferTransactionRepository;
    private final MoneyTransferMapper moneyTransferMapper;
    private final PaymentMapper paymentMapper;
    private final WalletService walletService;
    private final BankAccountService bankAccountService;
    private final PaymentService paymentService;
    private final OntopProperties ontopProperties;

    private static final String FEE_PERCENTAGE = "0.10";

    @Autowired
    public TransferServiceImpl(TransferTransactionRepository transferTransactionRepository,
                               MoneyTransferMapper moneyTransferMapper, PaymentMapper paymentMapper,
                               WalletService walletService, BankAccountService bankAccountService,
                               PaymentService paymentService, OntopProperties ontopProperties) {
        this.transferTransactionRepository = transferTransactionRepository;
        this.moneyTransferMapper = moneyTransferMapper;
        this.paymentMapper = paymentMapper;
        this.walletService = walletService;
        this.bankAccountService = bankAccountService;
        this.paymentService = paymentService;
        this.ontopProperties = ontopProperties;
    }

    @Override
    @Transactional(noRollbackFor= NotProcessedPaymentException.class)
    public MoneyTransferResponseDTO transfer(MoneyTransferRequestDTO moneyTransferRequestDTO) {
        log.debug("Starting money transfer service execution for {} at {}",
                moneyTransferRequestDTO.getIdempotencyKey(), LocalDateTime.now());

        //Validate transaction execution - Idempotency.
        Optional<TransferTransaction> optionalTransaction
                = transferTransactionRepository.findByIdempotencyKey(moneyTransferRequestDTO.getIdempotencyKey());
        if(optionalTransaction.isPresent()) {
            TransferTransaction existentTransaction = optionalTransaction.get();
            log.info("Transfer transaction already executed with id: {}", existentTransaction.getTransactionId());
            return moneyTransferMapper.toMoneyTransferResponseDTO(existentTransaction);
        }

        log.info("Executing new transfer for idempotency key: {}", moneyTransferRequestDTO.getIdempotencyKey());

        //Withdraw from the user's wallet.
        WalletTransactionDTO walletTransactionDTO = walletService.withdraw(moneyTransferRequestDTO);
        log.info("Withdrawn from wallet with wallet transaction id: {}", walletTransactionDTO.getWalletTransactionId());

        //Adjust amount discounting a fee.
        BigDecimal feeAmount =
                moneyTransferRequestDTO.getTransactionAmount().multiply(new BigDecimal(FEE_PERCENTAGE));

        final BigDecimal adjustedAmount = moneyTransferRequestDTO.getTransactionAmount().subtract(feeAmount);
        log.info("Amount calculated after fee: {}", adjustedAmount);

        //Execute Bank transfer from Ontop's account to user's account
        BankAccountDTO bankAccountDTO =
                bankAccountService.getBankAccountByWalletId(moneyTransferRequestDTO.getFromWalletId());

        PaymentRequestDTO paymentRequestDTO = paymentMapper.toPaymentRequestDTO(adjustedAmount, bankAccountDTO,
                ontopProperties);

        try {
            PaymentResponseDTO paymentResponseDTO = paymentService.payment(paymentRequestDTO);

            TransferTransaction newTransferTransaction =
                    moneyTransferMapper.toTransferTransaction(moneyTransferRequestDTO);
            newTransferTransaction.setTransactionDateTime(LocalDateTime.now());
            newTransferTransaction.setWalletTransactionId(walletTransactionDTO.getWalletTransactionId());
            newTransferTransaction.setPaymentTransactionId(paymentResponseDTO.getPaymentInfo().getId());
            newTransferTransaction.setPaymentStatus(paymentResponseDTO.getRequestInfo().getStatus());
            newTransferTransaction.setTransferredAmount(adjustedAmount);
            newTransferTransaction.setFeeAmount(feeAmount);

            TransferTransaction transferTransaction = transferTransactionRepository.save(newTransferTransaction);
            return moneyTransferMapper.toMoneyTransferResponseDTO(transferTransaction);
        } catch (NotProcessedPaymentException e) {
            log.error("Bank transfer failed while processing transfer with key: {} for wallet id: {}",
                    moneyTransferRequestDTO.getIdempotencyKey(), moneyTransferRequestDTO.getFromWalletId());

            //TODO: throw event to refund to wallet. Not implemented as it's a separated part of the process.

            TransferTransaction failedTransferTransaction =
                    moneyTransferMapper.toTransferTransaction(moneyTransferRequestDTO);
            failedTransferTransaction.setTransactionDateTime(LocalDateTime.now());
            failedTransferTransaction.setWalletTransactionId(walletTransactionDTO.getWalletTransactionId());
            failedTransferTransaction.setPaymentStatus("Failed");
            failedTransferTransaction.setFeeAmount(feeAmount);
            transferTransactionRepository.save(failedTransferTransaction);

            throw new NotProcessedPaymentException("Bank transfer failed while processing transfer");
        }
    }
}
