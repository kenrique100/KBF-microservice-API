package com.akentech.kbf.transaction;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TransactionServiceApplicationTests {
    public static void main(String[] args) {
        SpringApplication.from(TransactionServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
