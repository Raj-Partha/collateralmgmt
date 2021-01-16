package com.sec.lending.collateral.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@ToString
public class EligibilityResponse {
    private String borrowerName;
    private boolean eligible;
    private String message;
    private long shortage;
}
