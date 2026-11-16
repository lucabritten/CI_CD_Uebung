package de.htwsaar.workshop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GreetingServiceTest {
    private final GreetingService service = new GreetingService();

    @Test void saysHelloToStudent() {
        assertEquals("Hello, Ada!", service.greet("Ada"));
    }

    @Test void fallsBackForBlankName() {
        assertEquals("Hello, CI/CD!", service.greet("  "));
    }
}
