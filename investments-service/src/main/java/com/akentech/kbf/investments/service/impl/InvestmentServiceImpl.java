package com.akentech.kbf.investments.service.impl;

import com.akentech.kbf.investments.exception.InsufficientBalanceException;
import com.akentech.kbf.investments.exception.InvalidRequestException;
import com.akentech.kbf.investments.model.Investment;
import com.akentech.kbf.investments.repository.InvestmentRepository;
import com.akentech.kbf.investments.service.InvestmentService;
import com.akentech.kbf.investments.utils.LoggingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {

    private final InvestmentRepository investmentRepository;

    @Override
    public Mono<Investment> createInvestment(BigDecimal initialAmount, String createdBy) {
        if (initialAmount == null || initialAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new InvalidRequestException("Initial amount must be greater than zero"));
        }
        if (createdBy == null || createdBy.isBlank()) {
            return Mono.error(new InvalidRequestException("CreatedBy cannot be null or empty"));
        }

        Investment investment = Investment.builder()
                .initialAmount(initialAmount)
                .currentBalance(initialAmount)
                .createdBy(createdBy)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        return investmentRepository.save(investment)
                .doOnSuccess(i -> LoggingUtil.logInfo("Investment created successfully: " + i.getId()));
    }

    @Override
    public Mono<Investment> getInvestmentById(String id) {
        if (id == null || id.isBlank()) {
            return Mono.error(new InvalidRequestException("ID cannot be null or empty"));
        }
        return investmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new InvalidRequestException("Investment not found with ID: " + id)))
                .doOnSuccess(i -> LoggingUtil.logInfo("Fetched investment: " + i.getId()));
    }

    @Override
    public Flux<Investment> getAllInvestments() {
        return investmentRepository.findAll()
                .doOnComplete(() -> LoggingUtil.logInfo("Fetched all investments"));
    }

    @Override
    public Mono<Investment> updateInvestment(String id, BigDecimal newAmount) {
        if (id == null || id.isBlank()) {
            return Mono.error(new InvalidRequestException("ID cannot be null or empty"));
        }
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new InvalidRequestException("New amount must be greater than zero"));
        }

        return investmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new InvalidRequestException("Investment not found with ID: " + id)))
                .flatMap(investment -> {
                    investment.setCurrentBalance(newAmount);
                    investment.setUpdatedAt(LocalDate.now());
                    return investmentRepository.save(investment)
                            .doOnSuccess(i -> LoggingUtil.logInfo("Updated investment: " + i.getId()));
                });
    }

    @Override
    public Mono<Void> deleteInvestment(String id) {
        if (id == null || id.isBlank()) {
            return Mono.error(new InvalidRequestException("ID cannot be null or empty"));
        }
        return investmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new InvalidRequestException("Investment not found with ID: " + id)))
                .flatMap(existingInvestment -> investmentRepository.deleteById(id)
                        .doOnSuccess(v -> LoggingUtil.logInfo("Deleted investment: " + id)));
    }


    @Override
    public Mono<Investment> deductFromInvestment(String id, BigDecimal amount) {
        if (id == null || id.isBlank()) {
            return Mono.error(new InvalidRequestException("ID cannot be null or empty"));
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new InvalidRequestException("Amount must be greater than zero"));
        }

        return investmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new InvalidRequestException("Investment not found with ID: " + id)))
                .flatMap(investment -> {
                    if (investment.getCurrentBalance().compareTo(amount) < 0) {
                        return Mono.error(new InsufficientBalanceException("Insufficient investment balance"));
                    }
                    investment.setCurrentBalance(investment.getCurrentBalance().subtract(amount));
                    investment.setUpdatedAt(LocalDate.now());
                    return investmentRepository.save(investment)
                            .doOnSuccess(i -> LoggingUtil.logInfo("Deducted from investment: " + i.getId()));
                });
    }

    @Override
    public Mono<Investment> addToInvestment(String id, BigDecimal amount) {
        if (id == null || id.isBlank()) {
            return Mono.error(new InvalidRequestException("ID cannot be null or empty"));
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new InvalidRequestException("Amount must be greater than zero"));
        }

        return investmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new InvalidRequestException("Investment not found with ID: " + id)))
                .flatMap(investment -> {
                    investment.setCurrentBalance(investment.getCurrentBalance().add(amount));
                    investment.setUpdatedAt(LocalDate.now());
                    return investmentRepository.save(investment)
                            .doOnSuccess(i -> LoggingUtil.logInfo("Added to investment: " + i.getId()));
                });
    }
}