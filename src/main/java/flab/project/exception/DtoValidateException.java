package flab.project.exception;

import lombok.Getter;

@Getter
public class DtoValidateException extends RuntimeException {

    private ExceptionCode exceptionCode;

    public DtoValidateException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
