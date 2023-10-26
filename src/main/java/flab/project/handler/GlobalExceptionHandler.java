package flab.project.handler;

import flab.project.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<ErrorResponse> handleKakaoException(KakaoException e) {
        return ResponseEntity.status(e.getExceptionCode().getStatus()).body(ErrorResponse.of(e.getExceptionCode()));
    }
}
