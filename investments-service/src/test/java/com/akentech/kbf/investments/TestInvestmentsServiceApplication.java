package com.akentech.kbf.investments;

import org.springframework.boot.SpringApplication;
import org.testcontainers.utility.TestcontainersConfiguration;

public class TestInvestmentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(InvestmentsServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
