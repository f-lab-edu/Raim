package flab.project.dto;

import flab.project.util.ValidationUtils;
import lombok.Getter;

@Getter
public class UserLoginDto {

    public String email;

    public String password;

    public static void ValidateUserLoginDto(UserLoginDto userLoginDto) {
        ValidationUtils.validateEmail(userLoginDto.email);
        ValidationUtils.validatePassword(userLoginDto.password);
    }
}