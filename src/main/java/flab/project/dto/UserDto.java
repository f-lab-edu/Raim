package flab.project.dto;

import flab.project.domain.User;
import flab.project.util.ValidationUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "유저 DTO")
public class UserDto {

    @NotEmpty
    @Schema(name = "name", description = "User 이름", example = "김개발", requiredMode = RequiredMode.REQUIRED)
    private String name;

    @Schema(name = "nickname", description = "User 닉네임", example = "개발남")
    private String nickname;

    @NotEmpty
    @Schema(name = "password", description = "User 비밀번호", example = "asdf1234!", requiredMode = RequiredMode.REQUIRED)
    private String password;

    @NotEmpty
    @Schema(name = "confirmPassword", description = "User 비밀번호 확인", example = "asdf1234!", requiredMode = RequiredMode.REQUIRED)
    private String confirmPassword;

    public static User createUser(UserDto userDto, String email, String phoneNumber) {

        return User.builder()
                .email(email)
                .password(userDto.getPassword())
                .name(userDto.getName())
                .phoneNumber(phoneNumber)
                .nickname(userDto.getDefaultNickName())
                .build();
    }

    private String getDefaultNickName() {
        if (nickname == null) {
            return name;
        }

        return nickname;
    }

    public void validUserDto() {
        ValidationUtils.confirmPassword(this.password, this.confirmPassword);
        ValidationUtils.validatePassword(this.password);
        ValidationUtils.validateName(this.name);
    }
}
