package flab.project.dto;

import flab.project.domain.Terms;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "약관 Dto")
public class TermResponseDto {

    private String title;

    private String content;

    public static TermResponseDto of(Terms terms) {
        return new TermResponseDto(terms.getTitle(), terms.getContent());
    }
}
