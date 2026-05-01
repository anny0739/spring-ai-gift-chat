package gift.client;

public interface LLMChatClient {
    String chat(String systemPrompt, String userMessage);
}
