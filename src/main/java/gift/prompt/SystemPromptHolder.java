package gift.prompt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SystemPromptHolder {
    private final String prompt;

    public SystemPromptHolder(@Value("classpath:prompts/system.st") Resource resource) throws IOException {
        this.prompt = resource.getContentAsString(StandardCharsets.UTF_8);
    }

    public String get() { return prompt; }
}
