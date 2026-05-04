package gift.service;

import gift.exception.ChatException;
import gift.prompt.SystemPromptHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.retry.TransientAiException;
import gift.dto.ChatResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static gift.service.ChatService.ERROR_MESSAGE;
import static org.mockito.BDDMockito.then;

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

    @Mock
    SystemPromptHolder holder;

    @BeforeEach
    void setUp() {
        given(chatClientBuilder.build()).willReturn(chatClient);
        given(holder.get()).willReturn("당신은 선물 추천 도우미입니다.");
        chatService = new ChatService(chatClientBuilder, holder);
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
        assertThat(response.durationMs()).isGreaterThanOrEqualTo(0L);
        assertThat(response.requestId()).isNotNull();
        assertThat(response.sessionId()).isEqualTo(sessionId);
    }

    @Test
    void LLM_호출_실패시_안내_메시지를_반환한다() {
        given(chatClient.prompt()).willThrow(new TransientAiException("지연 발생"));

        assertThatThrownBy(() -> chatService.chat("test", null))
                .isInstanceOf(ChatException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @Test
    void LLM이_null을_반환하면_ChatException을_던진다() {
        given(chatClient.prompt()).willReturn(spec);
        given(spec.system(anyString())).willReturn(spec);
        given(spec.user(anyString())).willReturn(spec);
        given(spec.call()).willReturn(callResponseSpec);
        given(callResponseSpec.content()).willReturn(null);

        assertThatThrownBy(() -> chatService.chat("test", null))
                .isInstanceOf(ChatException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @Test
    void LLM_호출_실패시_ChatException을_던진다() {
        given(chatClient.prompt()).willThrow(new TransientAiException("지연 발생"));

        assertThatThrownBy(() -> chatService.chat("test", null))
                .isInstanceOf(ChatException.class)
                .hasMessage(ERROR_MESSAGE);
    }

}