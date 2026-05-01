package gift.client;

import org.springaicommunity.claude.agent.sdk.Query;
import org.springaicommunity.claude.agent.sdk.QueryOptions;

public class ClaudeLLMChatClient implements LLMChatClient {
    @Override
    public String chat(String systemPrompt, String userMessage) {
        return Query.text(userMessage,
                QueryOptions.builder()
                        .appendSystemPrompt(systemPrompt)
                        .build());
    }
}
