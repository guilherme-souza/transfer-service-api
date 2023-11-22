package com.ontop.transfer.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ConfigurationProperties(prefix = "ontop")
@Data
public class OntopProperties {
    @NotBlank(message = "Company Type property is missing.")
    private String companyType;
    @NotBlank(message = "Company Name property is missing.")
    private String companyName;
    @NotBlank(message = "Company Account Number property is missing.")
    private String accountNumber;
    @NotBlank(message = "Company Account Currency property is missing.")
    private String currency;
    @NotBlank(message = "Company Account Routing Number property is missing.")
    private String routingNumber;
}
