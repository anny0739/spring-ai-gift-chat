package gift.client;

import org.springframework.ai.chat.client.ChatClient;

public class GoogleLLMChatClient implements LLMChatClient {
    private final ChatClient chatClient;

    public GoogleLLMChatClient(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    @Override
    public String chat(String systemPrompt, String userMessage) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .content();
    }
}
