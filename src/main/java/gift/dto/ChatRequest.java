package gift.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChatRequest(@NotBlank String message, UUID sessionId) {
}
