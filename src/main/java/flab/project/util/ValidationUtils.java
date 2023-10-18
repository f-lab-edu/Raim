package flab.project.util;

import flab.project.exception.DtoValidateException;
import flab.project.exception.ExceptionCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static void validateEmail(String email) {
        final String EMAIL_REGEX = "(\\w\\.)*\\w+@(\\w+\\.)+[A-Za-z]{2,3}";

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new DtoValidateException(ExceptionCode.EMAIL_VALIDATION);
        }
    }

    public static void validatePassword(String password) {
        final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()-_+=])(?=.*\\d).{8,20}$";

        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            throw new DtoValidateException(ExceptionCode.PASSWORD_VALIDATION);
        }
    }

    public static void validateName(String name) {
        final String NAME_REGEX = "^[A-Za-zㄱ-힣]{2,10}$";

        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            throw new DtoValidateException(ExceptionCode.NAME_VALIDATION);
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        final String PHONE_NUMBER_REGEX = "^01([0|1|6|7|8|9])-(\\d{3}|\\d{4})-\\d{4}$";

        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (!matcher.matches()) {
            throw new DtoValidateException(ExceptionCode.PHONE_NUMBER_VALIDATION);
        }
    }
    public static void confirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new DtoValidateException(ExceptionCode.PASSWORD_CONFIRM);
        }
    }
}
