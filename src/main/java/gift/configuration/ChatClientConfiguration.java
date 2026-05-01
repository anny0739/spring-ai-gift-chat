package gift.configuration;

import gift.client.ClaudeLLMChatClient;
import gift.client.GoogleLLMChatClient;
import gift.client.LLMChatClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {
    @Bean
    @ConditionalOnProperty(name = "chat.provider", havingValue = "google", matchIfMissing = true)
    public LLMChatClient googleChatClient(ChatClient.Builder builder) {
        return new GoogleLLMChatClient(builder);
    }

    @Bean
    @ConditionalOnProperty(name = "chat.provider", havingValue = "claude")
    public LLMChatClient claudeChatClient() {
        return new ClaudeLLMChatClient();
    }
}
