package com.akentech.kbf.investments;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class InvestmentsServiceApplicationTests {
    @Test
    void contextLoads() {
    }

}
