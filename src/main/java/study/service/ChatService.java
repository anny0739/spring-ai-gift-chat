package study.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import study.dto.ChatResponse;

import java.util.UUID;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public static final String ERROR_MESSAGE = "다시 요청해주세요.";
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ChatResponse chat(String message, UUID sessionId) {
        UUID requestId = UUID.randomUUID();

        long start = System.currentTimeMillis();
        String responseMessage;
        try {
            responseMessage = chatClient.prompt()
                    .system("당신은 친절한 선물 추천 도우미입니다.")
                    .user(message)
                    .call()
                    .content();
        } catch (Exception ex) {
            responseMessage = ERROR_MESSAGE;
            logger.error(ex.getMessage());
        }

        long durationMs = System.currentTimeMillis() - start;

        logger.debug("사용자 요청 : sessionId = {}, requestId = {}, message = {}, durationMs = {}", sessionId, requestId, message, durationMs);

        return new ChatResponse(requestId, responseMessage, durationMs);
    }
}
