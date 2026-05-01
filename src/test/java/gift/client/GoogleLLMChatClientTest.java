package gift.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GoogleLLMChatClientTest {
    GoogleLLMChatClient googleLLMChatClient;
    @Mock
    ChatClient.Builder chatClientBuilder;
    @Mock
    ChatClient chatClient;
    @Mock
    ChatClient.ChatClientRequestSpec spec;
    @Mock
    ChatClient.CallResponseSpec callResponseSpec;

    @BeforeEach
    void setUp() {
        given(chatClientBuilder.build()).willReturn(chatClient);
        googleLLMChatClient = new GoogleLLMChatClient(chatClientBuilder);
    }

    @Test
    void LLM_응답을_반환한다() {
        String expected = "무선 이어폰을 추천합니다.";

        given(chatClient.prompt()).willReturn(spec);
        given(spec.system(anyString())).willReturn(spec);
        given(spec.user(anyString())).willReturn(spec);
        given(spec.call()).willReturn(callResponseSpec);
        given(callResponseSpec.content()).willReturn(expected);

        assertThat(googleLLMChatClient.chat("시스템 프롬프트", "친구 생일 선물 추천해줘"))
                .isEqualTo(expected);
    }

    @Test
    void LLM이_null을_반환하면_null을_반환한다() {
        given(chatClient.prompt()).willReturn(spec);
        given(spec.system(anyString())).willReturn(spec);
        given(spec.user(anyString())).willReturn(spec);
        given(spec.call()).willReturn(callResponseSpec);
        given(callResponseSpec.content()).willReturn(null);

        assertThat(googleLLMChatClient.chat("prompt", "message")).isNull();
    }

    @Test
    void LLM_호출_실패시_예외를_전파한다() {
        given(chatClient.prompt()).willThrow(new RuntimeException("API 오류"));

        assertThatThrownBy(() -> googleLLMChatClient.chat("prompt", "message"))
                .isInstanceOf(RuntimeException.class);
    }
}
