package com.renyibang.orderapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = com.renyibang.orderapi.OrderApiApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
