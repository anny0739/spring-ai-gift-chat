package gift.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<Object> globalException(ChatException ex, WebRequest request) {
        HttpStatus status = switch (ex) {
            case LLMUnavailableException e -> HttpStatus.SERVICE_UNAVAILABLE;
            case LLMInvalidResponseException e -> HttpStatus.INTERNAL_SERVER_ERROR;
            case ChatException e -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ProblemDetail detail = createProblemDetail(
                ex,
                status,
                ex.getMessage(),
                "호출에 이슈가 발생했습니다. 다시 시도하세요.",
                null,
                request);

        return createResponseEntity(detail, new HttpHeaders(), status, request);
    }
}
