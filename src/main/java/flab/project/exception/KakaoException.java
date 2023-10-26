package flab.project.exception;

import lombok.Getter;

@Getter
public class KakaoException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public KakaoException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
