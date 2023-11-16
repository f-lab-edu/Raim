package flab.project.util;

import flab.project.exception.ExceptionCode;

import flab.project.exception.KakaoException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern emailPattern = Pattern.compile("(\\w\\.)*\\w+@[\\w.-]+\\.[A-Za-z]{2,3}");
    private static final Pattern passwordPattern = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()-_+=])(?=.*\\d).{8,20}$");
    private static final Pattern namePattern = Pattern.compile("^[A-Za-zㄱ-힣]{2,10}$");
    private static final Pattern phoneNumberPattern = Pattern.compile("^01([0|1|6|7|8|9])(\\d{3}|\\d{4})\\d{4}$");

    private ValidationUtils() {
        throw new RuntimeException("Do Not Create");
    }

    public static void validateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);

        if (!matcher.matches()) {
            throw new KakaoException(ExceptionCode.EMAIL_VALIDATION);
        }
    }

    public static void validatePassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);

        if (!matcher.matches()) {
            throw new KakaoException(ExceptionCode.PASSWORD_VALIDATION);
        }
    }

    public static void validateName(String name) {
        Matcher matcher = namePattern.matcher(name);

        if (!matcher.matches()) {
            throw new KakaoException(ExceptionCode.NAME_VALIDATION);
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        Matcher matcher = phoneNumberPattern.matcher(phoneNumber);

        if (!matcher.matches()) {
            throw new KakaoException(ExceptionCode.PHONE_NUMBER_VALIDATION);
        }
    }

    public static void confirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new KakaoException(ExceptionCode.PASSWORD_CONFIRM);
        }
    }

    public static void validateEssentialTerms(boolean essentialTerms1, boolean essentialTerms2) {
        if (!essentialTerms1 || !essentialTerms2) {
            throw new KakaoException(ExceptionCode.ESSENTIAL_TERM_NOT_AGREEMENT);
        }
    }
}
