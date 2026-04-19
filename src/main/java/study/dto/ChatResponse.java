package study.dto;


import java.util.UUID;

public record ChatResponse(UUID requestId, String message, long durationMs) {
}
