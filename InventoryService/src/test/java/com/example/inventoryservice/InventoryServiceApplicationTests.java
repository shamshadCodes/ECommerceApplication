package com.example.inventoryservice;

import org.junit.jupiter.api.Test;

/**
 * Lightweight smoke test.
 *
 * Note: The full Spring context test was removed here to avoid requiring a real
 * MySQL database during unit-test runs. The actual application context is
 * covered via docker-compose and manual/integration testing.
 */
class InventoryServiceApplicationTests {

	@Test
	void contextLoads() {
		// No-op: verifies test infrastructure wiring only
	}

}
