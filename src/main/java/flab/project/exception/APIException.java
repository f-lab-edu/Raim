package flab.project.exception;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public APIException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
