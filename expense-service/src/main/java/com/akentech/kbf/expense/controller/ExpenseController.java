package com.akentech.kbf.expense.controller;

import com.akentech.kbf.expense.model.Expense;
import com.akentech.kbf.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public Flux<Expense> getAllExpenses() {
        log.info("Received GET request for all expenses");
        return expenseService.getAllExpenses()
                .doOnNext(expense -> log.info("Returning expense: {}", expense));
    }

    @GetMapping("/{id}")
    public Mono<Expense> getExpenseById(@PathVariable String id) {
        log.info("Received GET request for expense with ID: {}", id);
        return expenseService.getExpenseById(id)
                .doOnSuccess(expense -> log.info("Returning expense with Id: {}", expense))
                .doOnError(error -> log.error("Error fetching expense with ID {}: {}", id, error.getMessage()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Expense> createExpense(@RequestBody @Valid Expense expense) {
        log.info("Received POST request to create expense: {}", expense);
        return expenseService.createExpense(expense)
                .doOnSuccess(savedExpense -> log.info("Expense created successfully: {}", savedExpense))
                .doOnError(error -> log.error("Error creating expense: {}", error.getMessage()));
    }

    @PutMapping("/{id}")
    public Mono<Expense> updateExpense(@PathVariable String id, @RequestBody Expense expense) {
        log.info("Received PUT request to update expense with ID: {}", id);
        return expenseService.updateExpense(id, expense)
                .doOnSuccess(updatedExpense -> log.info("Expense updated successfully: {}", updatedExpense))
                .doOnError(error -> log.error("Error updating expense with ID {}: {}", id, error.getMessage()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteExpense(@PathVariable String id) {
        log.info("Received DELETE request for expense with ID: {}", id);
        return expenseService.deleteExpense(id)
                .doOnSuccess(v -> log.info("Expense with ID {} deleted successfully", id))
                .doOnError(error -> log.error("Error deleting expense with ID {}: {}", id, error.getMessage()));
    }
}