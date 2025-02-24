package com.akentech.kbf.investments;

import com.akentech.kbf.investments.model.Investment;
import com.akentech.kbf.investments.repository.InvestmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDate;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class InvestmentsControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        investmentRepository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        investmentRepository.deleteAll().block();
    }

    @Test
    void testCreateInvestment() {
        webTestClient.post()
                .uri("/api/investments?initialAmount=1000&createdBy=JohnDoe")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.initialAmount").isEqualTo(1000)
                .jsonPath("$.currentBalance").isEqualTo(1000)
                .jsonPath("$.createdBy").isEqualTo("JohnDoe");
    }

    @Test
    void testCreateInvestmentWithInvalidData() {
        webTestClient.post()
                .uri("/api/investments?initialAmount=0&createdBy=")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testGetInvestmentById() {
        Investment investment = Investment.builder()
                .initialAmount(BigDecimal.valueOf(1000))
                .currentBalance(BigDecimal.valueOf(1000))
                .createdBy("JohnDoe")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Investment savedInvestment = investmentRepository.save(investment).block();

        assert savedInvestment != null;
        webTestClient.get()
                .uri("/api/investments/" + savedInvestment.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedInvestment.getId())
                .jsonPath("$.initialAmount").isEqualTo(1000)
                .jsonPath("$.currentBalance").isEqualTo(1000)
                .jsonPath("$.createdBy").isEqualTo("JohnDoe");
    }

    @Test
    void testGetInvestmentByInvalidId() {
        webTestClient.get()
                .uri("/api/investments/invalid-id")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateInvestment() {
        Investment investment = Investment.builder()
                .initialAmount(BigDecimal.valueOf(1000))
                .currentBalance(BigDecimal.valueOf(1000))
                .createdBy("JohnDoe")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Investment savedInvestment = investmentRepository.save(investment).block();

        assert savedInvestment != null;
        webTestClient.put()
                .uri("/api/investments/" + savedInvestment.getId() + "?newAmount=1500")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.currentBalance").isEqualTo(1500);
    }

    @Test
    void testUpdateInvestmentWithInvalidData() {
        webTestClient.put()
                .uri("/api/investments/invalid-id?newAmount=0")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testDeleteInvestment() {
        Investment investment = Investment.builder()
                .initialAmount(BigDecimal.valueOf(1000))
                .currentBalance(BigDecimal.valueOf(1000))
                .createdBy("JohnDoe")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Investment savedInvestment = investmentRepository.save(investment).block();

        assert savedInvestment != null;
        webTestClient.delete()
                .uri("/api/investments/" + savedInvestment.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteInvestmentWithInvalidId() {
        webTestClient.delete()
                .uri("/api/investments/invalid-id")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testDeductFromInvestment() {
        Investment investment = Investment.builder()
                .initialAmount(BigDecimal.valueOf(1000))
                .currentBalance(BigDecimal.valueOf(1000))
                .createdBy("JohnDoe")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Investment savedInvestment = investmentRepository.save(investment).block();

        assert savedInvestment != null;
        webTestClient.put()
                .uri("/api/investments/" + savedInvestment.getId() + "/deduct?amount=500")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.currentBalance").isEqualTo(500);
    }

    @Test
    void testDeductFromInvestmentWithInsufficientBalance() {
        Investment investment = Investment.builder()
                .initialAmount(BigDecimal.valueOf(1000))
                .currentBalance(BigDecimal.valueOf(1000))
                .createdBy("JohnDoe")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Investment savedInvestment = investmentRepository.save(investment).block();

        assert savedInvestment != null;
        webTestClient.put()
                .uri("/api/investments/" + savedInvestment.getId() + "/deduct?amount=1500")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testAddToInvestment() {
        Investment investment = Investment.builder()
                .initialAmount(BigDecimal.valueOf(1000))
                .currentBalance(BigDecimal.valueOf(1000))
                .createdBy("JohnDoe")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        Investment savedInvestment = investmentRepository.save(investment).block();

        assert savedInvestment != null;
        webTestClient.put()
                .uri("/api/investments/" + savedInvestment.getId() + "/add?amount=500")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.currentBalance").isEqualTo(1500);
    }

    @Test
    void testAddToInvestmentWithInvalidData() {
        webTestClient.put()
                .uri("/api/investments/invalid-id/add?amount=0")
                .exchange()
                .expectStatus().isBadRequest();
    }
}