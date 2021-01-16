package com.sec.lending.collateral.entities;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Data
@Builder
@ToString
public class CollateralSummary implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String borrower;
    private long collateralValue;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedTime;
}
