package com.akentech.kbf.investments.repository;

import com.akentech.kbf.investments.model.Investment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface InvestmentRepository extends ReactiveMongoRepository<Investment, String> {
    Mono<Investment> findByCreatedBy(String createdBy);
}