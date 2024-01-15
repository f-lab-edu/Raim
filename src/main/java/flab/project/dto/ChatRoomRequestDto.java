package flab.project.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomRequestDto {

    private String roomName;
    private List<Long> users;

}
