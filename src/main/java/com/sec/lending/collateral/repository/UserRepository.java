package com.sec.lending.collateral.repository;

import com.sec.lending.collateral.entities.CollateralSummary;
import com.sec.lending.collateral.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByShortName(String shortName);
}
