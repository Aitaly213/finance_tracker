package org.example.finance_tracker.dto;

public record LoginResponse(
        String token,
        long expiresIn
) {
}
