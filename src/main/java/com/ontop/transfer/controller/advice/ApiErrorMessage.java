package com.ontop.transfer.controller.advice;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;
import java.util.List;

@Data
public class ApiErrorMessage {
    private HttpStatusCode status;
    private List<String> errors;

    public ApiErrorMessage(HttpStatusCode status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public ApiErrorMessage(HttpStatusCode status, String error) {
        this(status, Arrays.asList(error));
    }
}
