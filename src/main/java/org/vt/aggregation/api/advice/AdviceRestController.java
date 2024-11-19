package org.vt.aggregation.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vt.aggregation.api.dto.ErrorResponseDto;

@RestControllerAdvice
@Slf4j
public class AdviceRestController {

    @ExceptionHandler(Exception.class)
    public ErrorResponseDto handleException(Exception e) {
        log.error("Get error={}", e.getMessage(), e);
        return new ErrorResponseDto().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .errorMessage(e.getMessage());
    }
}
