package de.ketobi.vaadinspringdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class VaadinspringdemoApplicationTests {
	ApplicationContext applicationContext;
	@Test
	void contextLoads() {
	}

	// Verifies that the application context is not null
	@Test
	void contextNotNull() {
		assertNotNull(applicationContext);
	}

	@Test
	void contextContainsBeans() {
		assertTrue(applicationContext.containsBean("WorkflowRepository"));
	}
}
