package flab.project.exception;

import lombok.Getter;

@Getter
public class VerificationException extends RuntimeException {

    private ExceptionCode exceptionCode;

    public VerificationException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
