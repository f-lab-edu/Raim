package flab.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmailResponseDto {

    private String email;

    public static EmailResponseDto of(String email) {
        return new EmailResponseDto(email);
    }
}
