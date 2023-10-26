package flab.project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    USER_EXIST(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),

    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 중복되었습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다."),

    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일 인증 기록이 없습니다."),
    SMS_VERIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "SMS 인증 기록이 없습니다."),
    EMAIL_EXPIRED_VERIFICATION(HttpStatus.UNAUTHORIZED, "이메일 인증 유효기간이 지났습니다."),
    EMAIL_UNVERIFIED_VERIFICATION(HttpStatus.FORBIDDEN, "이메일 인증이 필요합니다."),
    SMS_EXPIRED_VERIFICATION(HttpStatus.UNAUTHORIZED, "SMS 인증 유효기간이 지났습니다."),
    SMS_UNVERIFIED_VERIFICATION(HttpStatus.FORBIDDEN, "SMS 인증이 필요합니다."),
    SMS_CODE_NOT_MATCH(HttpStatus.UNAUTHORIZED, "인증번호가 틀렸습니다."),

    EMAIL_VALIDATION(HttpStatus.BAD_REQUEST, "이메일 형식이 맞지 않습니다."),
    PASSWORD_VALIDATION(HttpStatus.BAD_REQUEST, "비밀번호 형식이 맞지 않습니다."),
    NAME_VALIDATION(HttpStatus.BAD_REQUEST, "이름 형식이 맞지 않습니다."),
    PHONE_NUMBER_VALIDATION(HttpStatus.BAD_REQUEST, "휴대전화 형식이 맞지 않습니다."),
    PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "비밀번호와 확인 비밀번호가 일치하지 않습니다."),

    REQUEST_TOO_FAST(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 빠릅니다."),

    API_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "외부 API를 처리하지 못했습니다.");

    private final HttpStatus status;

    private final String message;
}
