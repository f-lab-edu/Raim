package flab.project.dto;

import flab.project.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getPhoneNumber());
    }
}
