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
    SMS_CODE_NOT_MATCH(HttpStatus.UNAUTHORIZED, "인증번호가 틀렸습니다.");

    private final HttpStatus status;

    private final String message;
}
