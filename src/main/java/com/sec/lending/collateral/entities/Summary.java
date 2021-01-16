package com.sec.lending.collateral.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Summary {
    private String borrower;
    private Long balance;
}
