package com.sec.lending.collateral.entities;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Data
@Builder
public class CollateralTransaction implements Serializable {
    @Id
    @GeneratedValue
    private long transactionID;
    private String borrower;
    private String custodian;

    private String collateralType;
    private long collateralValue;
    private String transactionDetails;
    @CreationTimestamp
    private LocalDateTime createDateTime;
}
