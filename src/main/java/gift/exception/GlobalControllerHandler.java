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
        ProblemDetail detail = createProblemDetail(
                ex,
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                "호출에 이슈가 발생했습니다. 다시 시도하세요.",
                null,
                request);

        return createResponseEntity(detail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
