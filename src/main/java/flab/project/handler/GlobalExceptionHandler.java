package flab.project.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import flab.project.dto.CommonResponseDto;
import flab.project.exception.DuplicateEmailException;
import flab.project.exception.EmailNotFoundException;
import flab.project.exception.ErrorResponse;
import flab.project.exception.VerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CommonResponseDto> handleDuplicateEmailException(DuplicateEmailException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponseDto.of(ErrorResponse.of(e.getExceptionCode())));
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CommonResponseDto> handleNotFoundEmailException(EmailNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponseDto.of(ErrorResponse.of(e.getExceptionCode())));
    }

    @ExceptionHandler(VerificationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CommonResponseDto> handleNotFoundVerificationException(VerificationException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponseDto.of(ErrorResponse.of(e.getExceptionCode())));
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<CommonResponseDto> handleUnsupportedEncodingException(UnsupportedEncodingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponseDto.of(ErrorResponse.of(HttpStatus.INSUFFICIENT_STORAGE, e.getMessage())));
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<CommonResponseDto> handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponseDto.of(ErrorResponse.of(HttpStatus.INSUFFICIENT_STORAGE, e.getMessage())));
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<CommonResponseDto> handleInvalidKeyException(InvalidKeyException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponseDto.of(ErrorResponse.of(HttpStatus.INSUFFICIENT_STORAGE, e.getMessage())));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<CommonResponseDto> JsonProcessingException(JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponseDto.of(ErrorResponse.of(HttpStatus.INSUFFICIENT_STORAGE, e.getMessage())));
    }
}
