package flab.project.dto;

import flab.project.domain.User;
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
    private String name;

    private String nickname;

    public static User getUser(UserDto userDto) {
        if (userDto.getNickname() == null) {
            userDto.nickname = userDto.name;
        }
        return User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getNickname())
                .build();
    }
}
