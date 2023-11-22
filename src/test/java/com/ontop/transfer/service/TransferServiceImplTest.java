package com.ontop.transfer.service;

import com.ontop.transfer.configuration.OntopProperties;
import com.ontop.transfer.exception.NotProcessedPaymentException;
import com.ontop.transfer.feign.bank.dto.BankAccountDTO;
import com.ontop.transfer.feign.payment.dto.*;
import com.ontop.transfer.feign.wallet.dto.WalletTransactionDTO;
import com.ontop.transfer.mapper.MoneyTransferMapper;
import com.ontop.transfer.mapper.PaymentMapper;
import com.ontop.transfer.repository.TransferTransactionRepository;
import com.ontop.transfer.repository.entity.TransferTransaction;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import com.ontop.transfer.service.dto.MoneyTransferResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

    @Mock
    private TransferTransactionRepository transferTransactionRepository;

    @Mock
    private MoneyTransferMapper moneyTransferMapper;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private WalletService walletService;

    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private OntopProperties ontopProperties;

    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testTransferSuccess() throws NotProcessedPaymentException {
        MoneyTransferRequestDTO requestDTO = mockMoneyTransferRequest();
        WalletTransactionDTO walletTransactionDTO = mockWalletTransaction();
        BankAccountDTO bankAccountDTO = mockBankAccount();
        PaymentRequestDTO paymentRequestDTO = mockPaymentRequest();
        PaymentResponseDTO paymentResponseDTO = mockPaymentResponse();

        when(walletService.withdraw(requestDTO)).thenReturn(walletTransactionDTO);
        when(bankAccountService.getBankAccountByWalletId(anyLong())).thenReturn(bankAccountDTO);
        when(paymentMapper.toPaymentRequestDTO(any(), any(), any())).thenReturn(paymentRequestDTO);
        when(paymentService.payment(paymentRequestDTO)).thenReturn(paymentResponseDTO);
        when(moneyTransferMapper.toTransferTransaction(requestDTO)).thenReturn(new TransferTransaction());
        when(moneyTransferMapper.toMoneyTransferResponseDTO(any())).thenReturn(mockMoneyTransferResponse());

        MoneyTransferResponseDTO result = transferService.transfer(requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getTransactionId());
        assertEquals("Testing Key", result.getIdempotencyKey());
        verify(transferTransactionRepository, atMostOnce()).save(any());
    }

    @Test
    public void testTransferIdempotentTransaction() {
        MoneyTransferRequestDTO requestDTO = mockMoneyTransferRequest();
        TransferTransaction existingTransaction = mockTransferTransaction();

        when(transferTransactionRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.of(existingTransaction));
        when(moneyTransferMapper.toMoneyTransferResponseDTO(any())).thenReturn(mockMoneyTransferResponse());

        MoneyTransferResponseDTO result = transferService.transfer(requestDTO);

        assertNotNull(result);
        assertEquals(existingTransaction.getTransactionId(), result.getTransactionId());
    }

    @Test
    public void testTransferFailedPayment() {
        MoneyTransferRequestDTO requestDTO = mockMoneyTransferRequest();
        WalletTransactionDTO walletTransactionDTO = mockWalletTransaction();
        BankAccountDTO bankAccountDTO = mockBankAccount();
        PaymentRequestDTO paymentRequestDTO = mockPaymentRequest();

        when(walletService.withdraw(requestDTO)).thenReturn(walletTransactionDTO);
        when(bankAccountService.getBankAccountByWalletId(anyLong())).thenReturn(bankAccountDTO);
        when(paymentMapper.toPaymentRequestDTO(any(), any(), any())).thenReturn(paymentRequestDTO);
        when(paymentService.payment(paymentRequestDTO)).thenThrow(NotProcessedPaymentException.class);
        when(moneyTransferMapper.toTransferTransaction(requestDTO)).thenReturn(new TransferTransaction());

        assertThrows(NotProcessedPaymentException.class, () -> transferService.transfer(requestDTO));
    }

    private MoneyTransferRequestDTO mockMoneyTransferRequest() {
        MoneyTransferRequestDTO moneyTransferRequestDTO = new MoneyTransferRequestDTO();
        moneyTransferRequestDTO.setFromWalletId(1L);
        moneyTransferRequestDTO.setIdempotencyKey("Testing Key");
        moneyTransferRequestDTO.setTransactionAmount(BigDecimal.TEN);
        return moneyTransferRequestDTO;
    }

    private TransferTransaction mockTransferTransaction() {
        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setTransactionId(1L);
        transferTransaction.setIdempotencyKey("Testing Key");
        transferTransaction.setTransactionAmount(BigDecimal.TEN);
        return transferTransaction;
    }

    private MoneyTransferResponseDTO mockMoneyTransferResponse() {
        MoneyTransferResponseDTO moneyTransferResponseDTO = new MoneyTransferResponseDTO();
        moneyTransferResponseDTO.setIdempotencyKey("Testing Key");
        moneyTransferResponseDTO.setTransactionAmount(BigDecimal.TEN);
        moneyTransferResponseDTO.setTransactionId(1L);
        return moneyTransferResponseDTO;
    }

    private WalletTransactionDTO mockWalletTransaction() {
        WalletTransactionDTO walletTransactionDTO = new WalletTransactionDTO();
        walletTransactionDTO.setUserId(1L);
        walletTransactionDTO.setAmount(BigDecimal.TEN);
        walletTransactionDTO.setWalletTransactionId(1L);

        return walletTransactionDTO;
    }

    private BankAccountDTO mockBankAccount() {
        return new BankAccountDTO();
    }

    private PaymentRequestDTO mockPaymentRequest() {
        return new PaymentRequestDTO();
    }

    private PaymentResponseDTO mockPaymentResponse() {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
        PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
        paymentInfoDTO.setId("Pay ID");
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        requestInfoDTO.setStatus("Processing");
        paymentResponseDTO.setPaymentInfo(paymentInfoDTO);
        paymentResponseDTO.setRequestInfo(requestInfoDTO);
        return paymentResponseDTO;
    }
}
