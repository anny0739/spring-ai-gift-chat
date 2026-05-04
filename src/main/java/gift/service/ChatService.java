package gift.service;

import gift.prompt.SystemPromptHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import gift.dto.ChatResponse;
import gift.exception.ChatException;

import java.util.UUID;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final String systemPrompt;

    public static final String ERROR_MESSAGE = "다시 요청해주세요.";
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(ChatClient.Builder builder, SystemPromptHolder promptHolder) {
        this.chatClient = builder.build();
        this.systemPrompt = promptHolder.get();
    }

    public ChatResponse chat(String message, UUID sessionId) {
        UUID requestId = UUID.randomUUID();

        long start = System.currentTimeMillis();
        String responseMessage;
        try {
            responseMessage = chatClient.prompt()
                    .system(systemPrompt)
                    .user(message)
                    .call()
                    .content();
        } catch (Exception ex) {
            logger.error("AI 호출 실패: sessionId={}, requestId={}, message={}", sessionId, requestId, message, ex);
            throw new ChatException(ERROR_MESSAGE);
        }

        if (responseMessage == null) {
            logger.warn("AI 응답이 null: sessionId={}, requestId={}", sessionId, requestId);
            throw new ChatException(ERROR_MESSAGE);
        }


        long durationMs = System.currentTimeMillis() - start;

        logger.info("사용자 요청 : sessionId = {}, requestId = {}, message = {}, durationMs = {}", sessionId, requestId, message, durationMs);

        return new ChatResponse(requestId, responseMessage, durationMs, sessionId);
    }
}
