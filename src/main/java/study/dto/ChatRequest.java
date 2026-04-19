package study.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChatRequest(@NotBlank String message, UUID sessionId) {
}
