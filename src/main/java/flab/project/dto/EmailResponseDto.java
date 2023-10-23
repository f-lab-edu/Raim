package flab.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "이메일 Dto")
public class EmailResponseDto {

    @Schema(description = "이메일", example = "asd123@asd.com")
    private String email;

    public static EmailResponseDto of(String email) {
        return new EmailResponseDto(email);
    }
}
