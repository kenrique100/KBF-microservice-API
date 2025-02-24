package com.akentech.kbf.investments.controller;

import com.akentech.kbf.investments.model.Investment;
import com.akentech.kbf.investments.service.InvestmentService;
import com.akentech.kbf.investments.utils.LoggingUtil;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Investment> createInvestment(
            @RequestParam @NotNull @DecimalMin(value = "0.01", message = "Initial amount must be greater than zero") BigDecimal initialAmount,
            @RequestParam @NotBlank(message = "CreatedBy cannot be blank") String createdBy) {
        return investmentService.createInvestment(initialAmount, createdBy)
                .doOnSuccess(i -> LoggingUtil.logInfo("Investment created successfully: " + i.getId()))
                .doOnError(e -> LoggingUtil.logError("Error creating investment: " + e.getMessage()));
    }

    @GetMapping("/{id}")
    public Mono<Investment> getInvestmentById(@PathVariable @NotBlank(message = "ID cannot be blank") String id) {
        return investmentService.getInvestmentById(id)
                .doOnSuccess(i -> LoggingUtil.logInfo("Fetched investment: " + i.getId()))
                .doOnError(e -> LoggingUtil.logError("Error fetching investment: " + e.getMessage()));
    }

    @GetMapping
    public Flux<Investment> getAllInvestments() {
        return investmentService.getAllInvestments()
                .doOnComplete(() -> LoggingUtil.logInfo("Fetched all investments"))
                .doOnError(e -> LoggingUtil.logError("Error fetching all investments: " + e.getMessage()));
    }

    @PutMapping("/{id}")
    public Mono<Investment> updateInvestment(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @RequestParam @NotNull @DecimalMin(value = "0.01", message = "New amount must be greater than zero") BigDecimal newAmount) {
        return investmentService.updateInvestment(id, newAmount)
                .doOnSuccess(i -> LoggingUtil.logInfo("Updated investment: " + i.getId()))
                .doOnError(e -> LoggingUtil.logError("Error updating investment: " + e.getMessage()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteInvestment(@PathVariable @NotBlank(message = "ID cannot be blank") String id) {
        return investmentService.deleteInvestment(id)
                .doOnSuccess(v -> LoggingUtil.logInfo("Deleted investment: " + id))
                .doOnError(e -> LoggingUtil.logError("Error deleting investment: " + e.getMessage()));
    }

    @PutMapping("/{id}/deduct")
    public Mono<Investment> deductFromInvestment(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @RequestParam @NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount) {
        return investmentService.deductFromInvestment(id, amount)
                .doOnSuccess(i -> LoggingUtil.logInfo("Deducted from investment: " + i.getId()))
                .doOnError(e -> LoggingUtil.logError("Error deducting from investment: " + e.getMessage()));
    }

    @PutMapping("/{id}/add")
    public Mono<Investment> addToInvestment(
            @PathVariable @NotBlank(message = "ID cannot be blank") String id,
            @RequestParam @NotNull @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount) {
        return investmentService.addToInvestment(id, amount)
                .doOnSuccess(i -> LoggingUtil.logInfo("Added to investment: " + i.getId()))
                .doOnError(e -> LoggingUtil.logError("Error adding to investment: " + e.getMessage()));
    }
}