package flab.project.exception;

import lombok.Getter;

@Getter
public class UserExistsException extends RuntimeException{

    private ExceptionCode exceptionCode;

    public UserExistsException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
