package flab.project.dto;

import flab.project.domain.User;
import flab.project.util.ValidationUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    @NotEmpty
    private String name;

    @NotEmpty
    private String phoneNumber;

    private String nickname;

    public static User getUser(UserDto userDto) {
        if (userDto.getNickname() == null) {
            userDto.nickname = userDto.name;
        }
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .nickname(userDto.getNickname())
                .build();
    }

    public static void validUserDto(UserDto userDto) {
        ValidationUtils.validateEmail(userDto.getEmail());
        ValidationUtils.confirmPassword(userDto.getPassword(), userDto.getConfirmPassword());
        ValidationUtils.validatePassword(userDto.getPassword());
        ValidationUtils.validateName(userDto.getName());
        ValidationUtils.validatePhoneNumber(userDto.getPhoneNumber());
    }
}
