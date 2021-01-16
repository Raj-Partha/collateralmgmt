package com.sec.lending.collateral.api;

import com.sec.lending.collateral.entities.*;
import com.sec.lending.collateral.repository.CollateralSummayRepository;
import com.sec.lending.collateral.repository.CollateralTransactionRepository;
import com.sec.lending.collateral.repository.UserRepository;
import com.sec.lending.collateral.request.EligibilityRequest;
import com.sec.lending.collateral.response.EligibilityResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api"})
@Log4j2
public class APIController {
    private static final long STOCK_CMP = 10;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollateralSummayRepository summaryRepository;

    @Autowired
    private CollateralTransactionRepository transactionRepository;

    @GetMapping("/login/get-all-users")
    public Iterable<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(nodeDetails -> {
            if (!CollectionUtils.isEmpty(nodeDetails.getRoles()) && nodeDetails.getRoles().contains(Roles.Lender)) {
                User node = User.builder().id(nodeDetails.getId()).shortName(nodeDetails.getShortName()).roles(nodeDetails.getRoles()).build();
                users.add(node);
            }
        });

        return users;
    }

    @GetMapping("/login/get-all-borrowers")
    public Iterable<User> getAllBorrowers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(nodeDetails -> {
            if (!CollectionUtils.isEmpty(nodeDetails.getRoles()) && nodeDetails.getRoles().contains(Roles.Borrower)) {
                User node = User.builder().id(nodeDetails.getId()).shortName(nodeDetails.getShortName()).roles(nodeDetails.getRoles()).build();
                users.add(node);
            }
        });

        return users;
    }


    //Mock implementation
    @PostMapping("/collateral/return-collateral")
    public CollateralTransaction returnCollateral(@RequestBody EligibilityRequest request) {

        CollateralSummary summary = summaryRepository.findByBorrower(request.getBorrowerName());
        if (summary == null) {
            summary = CollateralSummary.builder().borrower(request.getBorrowerName()).build();
        }
        long collateralValue = request.getNoOfStocks() * STOCK_CMP;

        summary.setCollateralValue(summary.getCollateralValue() + collateralValue);
        summaryRepository.save(summary);
        CollateralTransaction collateralTransaction = CollateralTransaction.builder().custodian(request.getCustodian()).collateralValue(collateralValue)
                .borrower(request.getBorrowerName()).build();

        return transactionRepository.save(collateralTransaction);
    }

    //Mock implementation
    @PostMapping("/collateral/check-eligibility")
    public EligibilityResponse checkEligibility(@RequestBody EligibilityRequest request) {
        boolean isEligible = false;
        CollateralSummary summary = summaryRepository.findByBorrower(request.getBorrowerName());
        long requestedValue = request.getNoOfStocks() * STOCK_CMP;
        long collateralValue = 0;
        if (summary != null) {
            collateralValue = summary.getCollateralValue();
            if (requestedValue <= collateralValue) {
                summary.setCollateralValue(summary.getCollateralValue() - requestedValue);
                summaryRepository.save(summary);
                CollateralTransaction deduced_for_ledger = CollateralTransaction.builder().borrower(request.getBorrowerName()).transactionDetails("deduced for ledger")
                        .collateralValue(requestedValue * -1).custodian(request.getCustodian()).build();
                transactionRepository.save(deduced_for_ledger);
                isEligible = true;
            }
        }
        EligibilityResponse eligibilityResponse = EligibilityResponse.builder()
                .borrowerName(request.getBorrowerName())
                .eligible(isEligible)
                .message(isEligible ? "Eligible" : "Insufficient fund, with a shortage of $" + Math.abs(collateralValue - requestedValue))
                .shortage(isEligible ? 0 : Math.abs(collateralValue - requestedValue)).build();
        log.info("Response : " + eligibilityResponse.toString());
        return eligibilityResponse;
    }


    @PutMapping("/collateral/add")
    @Transactional
    public CollateralTransaction addCollateral(@RequestBody CollateralTransaction collateralTransaction) {
        System.out.println("saving transaction " + collateralTransaction);
        CollateralSummary summary = summaryRepository.findByBorrower(collateralTransaction.getBorrower());
        if (summary == null) {
            summary = CollateralSummary.builder().borrower(collateralTransaction.getBorrower()).build();
        }
        summary.setCollateralValue(summary.getCollateralValue() + collateralTransaction.getCollateralValue());
        summaryRepository.save(summary);
        return transactionRepository.save(collateralTransaction);
    }

    @GetMapping("/collateral/transactions")
    public Iterable<CollateralTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/collateral/summary")
    public Iterable<CollateralSummary> getCollateralSummary() {
        return summaryRepository.findAll();
    }
}
