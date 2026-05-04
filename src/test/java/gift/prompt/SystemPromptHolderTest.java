package gift.prompt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SystemPromptHolderTest {

    @Test
    void 클래스패스_파일에서_시스템_프롬프트를_로드한다() throws IOException {
        Resource resource = new ClassPathResource("prompts/system.st");
        SystemPromptHolder holder = new SystemPromptHolder(resource);

        assertThat(holder.get()).contains("선물 추천");
    }
}