package com.ontop.transfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ontop.transfer.controller.request.MoneyTransferRequest;
import com.ontop.transfer.controller.response.MoneyTransferResponse;
import com.ontop.transfer.mapper.MoneyTransferMapper;
import com.ontop.transfer.service.TransferService;
import com.ontop.transfer.service.dto.MoneyTransferRequestDTO;
import com.ontop.transfer.service.dto.MoneyTransferResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService transferService;

    @Autowired
    private MoneyTransferMapper moneyTransferMapper;

    @Test
    public void testMoneyTransfer() throws Exception {
        MoneyTransferRequest request = mockMoneyTransferRequest();
        MoneyTransferResponseDTO responseDTO = mockMoneyTransferResponseDTO();

        when(transferService.transfer(any())).thenReturn(responseDTO);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/money-transfers")
                        .header("Idempotency-Key", "idempotency-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaction_id").value(responseDTO.getTransactionId()));
    }

    private MoneyTransferRequest mockMoneyTransferRequest() {
        MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
        moneyTransferRequest.setFromWalletId(1L);
        moneyTransferRequest.setTransactionAmount(BigDecimal.TEN);
        return moneyTransferRequest;
    }

    private MoneyTransferResponseDTO mockMoneyTransferResponseDTO() {
        MoneyTransferResponseDTO moneyTransferResponseDTO = new MoneyTransferResponseDTO();
        moneyTransferResponseDTO.setTransactionId(1L);
        return moneyTransferResponseDTO;
    }
}

