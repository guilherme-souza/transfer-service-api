package com.ontop.transfer.feign;

import com.ontop.transfer.exception.BusinessLogicException;
import com.ontop.transfer.exception.InternalErrorException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private static final String WALLET_CLIENT_METHOD_KEY = "WalletClient";
    private static final String BANK_ACCOUNT_CLIENT_METHOD_KEY = "BankAccountClient";
    private static final String NOT_FOUND_MESSAGE = "It was not possible to identify a wallet. Wallet not found.";
    private static final String BANK_ACCOUNT_NOT_FOUND_MESSAGE = "Recipient's bank account not found.";
    private static final String BAD_REQUEST_MESSAGE = "Invalid body.";
    private static final String INTERNAL_ERROR_MESSAGE =
            "An internal error was identified while processing the money transfer.";

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new InternalErrorException(getBadRequestMessage(methodKey));
            case 404 -> new BusinessLogicException(getNotFoundMessage(methodKey));
            case 500 -> new InternalErrorException(getInternalErrorMessage(methodKey));
            default -> FeignException.errorStatus(methodKey, response);
        };
    }

    private static String getNotFoundMessage(String methodKey) {
        log.error("Client Service: {} responded not found.", methodKey);
        if(BANK_ACCOUNT_CLIENT_METHOD_KEY.startsWith(methodKey)) {
            return BANK_ACCOUNT_NOT_FOUND_MESSAGE;
        }

        return NOT_FOUND_MESSAGE;
    }

    private static String getBadRequestMessage(String methodKey) {
        log.error("Client Service: {} responded bad request.", methodKey);
        return BAD_REQUEST_MESSAGE;
    }

    private static String getInternalErrorMessage(String methodKey) {
        log.error("Client Service: {} responded internal error service.", methodKey);
        return INTERNAL_ERROR_MESSAGE;
    }
}
