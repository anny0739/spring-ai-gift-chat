package study.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.retry.TransientAiException;
import study.dto.ChatResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static study.service.ChatService.ERROR_MESSAGE;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Mock
    ChatClient.Builder chatClientBuilder;

    @Mock
    ChatClient chatClient;

    ChatService chatService;

    @Mock
    ChatClient.ChatClientRequestSpec spec;

    @Mock
    ChatClient.CallResponseSpec callResponseSpec;

    @BeforeEach
    void setUp() {
        given(chatClientBuilder.build()).willReturn(chatClient);
        chatService = new ChatService(chatClientBuilder);
    }

    @Test
    void LLM_응답을_반환한다() {
        final String message = "test";
        final UUID sessionId = null;
        final String responseMessage = "추천 선물은 ...";

        given(chatClient.prompt()).willReturn(spec);
        given(spec.system(anyString())).willReturn(spec);
        given(spec.user(anyString())).willReturn(spec);

        given(spec.call()).willReturn(callResponseSpec);
        given(callResponseSpec.content()).willReturn(responseMessage);

        ChatResponse response = chatService.chat(message, sessionId);

        assertThat(response.message()).isEqualTo(responseMessage);
        assertThat(response.durationMs()).isGreaterThan(0L);
        assertThat(response.requestId()).isNotNull();
    }

    @Test
    void LLM_호출_실패시_안내_메시지를_반환한다() {
        final String message = "test";
        final UUID sessionId = null;

        given(chatClient.prompt()).willThrow(new TransientAiException("지연 발생"));

        ChatResponse result = chatService.chat(message, sessionId);

        assertThat(ERROR_MESSAGE).isEqualTo(result.message());
    }
}