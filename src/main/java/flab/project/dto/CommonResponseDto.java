package flab.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommonResponseDto<T> {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> CommonResponseDto<T> of(String message, T data) {
        return new CommonResponseDto<>(message, data);
    }
}
