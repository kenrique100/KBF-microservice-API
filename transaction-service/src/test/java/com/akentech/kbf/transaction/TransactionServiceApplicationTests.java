package com.akentech.kbf.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestContainersConfiguration.class)
@SpringBootTest
public class TransactionServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}