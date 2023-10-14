package flab.project.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException{

    private ExceptionCode exceptionCode;

    public DuplicateEmailException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
