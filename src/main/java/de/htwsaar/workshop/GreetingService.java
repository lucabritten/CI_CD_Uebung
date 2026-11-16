package de.htwsaar.workshop;

public final class GreetingService {
    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, CI/CD!";
        }
        return "Hello, " + name.trim() + "!";
    }
}
