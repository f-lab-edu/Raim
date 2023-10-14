package flab.project.exception;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {

    private ExceptionCode exceptionCode;

    public EmailNotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
