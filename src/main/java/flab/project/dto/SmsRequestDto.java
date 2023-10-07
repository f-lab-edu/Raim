package flab.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class SmsRequestDto {

    private String type;
    private String contentType;
    private String countryCode;
    private String from;
    private String content;
    List<MessageDto> messages;

    @AllArgsConstructor
    @Getter
    @Builder
    static public class MessageDto {
        private String to;
        private String content;
    }
}
