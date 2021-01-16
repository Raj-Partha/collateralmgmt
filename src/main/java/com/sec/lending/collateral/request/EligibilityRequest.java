package com.sec.lending.collateral.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class EligibilityRequest {
    private String symbol;
    private long noOfStocks;
    private double currentMarketPrice;
    private String borrowerName;
    private String custodian;
}
