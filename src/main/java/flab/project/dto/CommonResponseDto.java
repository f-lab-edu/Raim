package flab.project.dto;

import flab.project.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommonResponseDto<T> {
    private int status;
    private String message;
    private T data;

    public static <T> CommonResponseDto<T> of(int status, String message, T data) {
        return new CommonResponseDto<>(status, message, data);
    }
}
