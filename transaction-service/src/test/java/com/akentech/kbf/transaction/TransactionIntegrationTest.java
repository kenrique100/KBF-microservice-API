/*
package com.akentech.kbf.transaction;

import com.akentech.kbf.expense.model.Expense;
import com.akentech.kbf.income.model.Income;
import com.akentech.kbf.investments.model.Investment;
import com.akentech.kbf.transaction.config.WebClientConfig;
import com.akentech.kbf.transaction.controller.TransactionController;
import com.akentech.kbf.transaction.model.Transaction;
import com.akentech.kbf.transaction.repository.TransactionRepository;
import com.akentech.kbf.transaction.service.impl.TransactionServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // ✅ Added JavaTimeModule for LocalDate support
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TransactionController.class)
@Import({TransactionServiceImpl.class, WebClientConfig.class})
public class TransactionIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private TransactionRepository transactionRepository;

    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() throws JsonProcessingException {
        // ✅ Corrected: Initialize ObjectMapper with JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String baseUrl = mockWebServer.url("/").toString();

        // ✅ Configure WebClient properties
        System.setProperty("income.service.url", baseUrl + "api/incomes");
        System.setProperty("expense.service.url", baseUrl + "api/expenses");
        System.setProperty("investment.service.url", baseUrl + "api/investments");

        // ✅ Mock external service responses using correct serialization
        Income income = new Income(new org.bson.types.ObjectId("650b1f9e8f1b2c3d4e5f6a7b"),
                "Salary", LocalDate.now(), 1, BigDecimal.valueOf(1000),
                BigDecimal.valueOf(1000), BigDecimal.ZERO, "receipt1", "user1");

        Expense expense = new Expense(new org.bson.types.ObjectId("650b1f9e8f1b2c3d4e5f6a7c"),
                "Rent", LocalDate.now(), 1, BigDecimal.valueOf(500),
                BigDecimal.valueOf(500), BigDecimal.ZERO, "receipt2", "user1");

        Investment investment = new Investment("3", BigDecimal.valueOf(200), BigDecimal.valueOf(200),
                "user1", LocalDate.now(), LocalDate.now());

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(List.of(income))) // ✅ Fixed serialization
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(List.of(expense))) // ✅ Fixed serialization
                .addHeader("Content-Type", "application/json"));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(List.of(investment))) // ✅ Fixed serialization
                .addHeader("Content-Type", "application/json"));

        // ✅ Mock repository saveAll method
        when(transactionRepository.saveAll(anyIterable()))
                .thenReturn(Flux.just(
                        new Transaction("INCOME", "1", LocalDate.now(), BigDecimal.valueOf(1000), "user1"),
                        new Transaction("EXPENSE", "2", LocalDate.now(), BigDecimal.valueOf(500), "user1"),
                        new Transaction("INVESTMENT", "3", LocalDate.now(), BigDecimal.valueOf(200), "user1")
                ));
    }

    @Test
    void getDashboardData_ValidRequest_ReturnsDashboardReport() {
        webTestClient.get()
                .uri("/api/transactions/dashboard?range=30")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.totalIncome").isEqualTo(1000)
                .jsonPath("$.totalExpense").isEqualTo(500)
                .jsonPath("$.totalInvestment").isEqualTo(200)
                .jsonPath("$.netGain").isEqualTo(300)
                .jsonPath("$.netLoss").isEqualTo(0);
    }
}
*/
