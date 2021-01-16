package com.sec.lending.collateral.repository;


import com.sec.lending.collateral.entities.CollateralTransaction;
import com.sec.lending.collateral.entities.Summary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CollateralTransactionRepository extends CrudRepository<CollateralTransaction, Long> {
    @Query("SELECT borrower as borrower, sum(collateralValue) as balance from CollateralTransaction group by borrower")
    Object summary();
}