package com.sec.lending.collateral.repository;

import com.sec.lending.collateral.entities.CollateralSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CollateralSummayRepository extends CrudRepository<CollateralSummary, Long> {
        CollateralSummary findByBorrower(String borrower);
}
