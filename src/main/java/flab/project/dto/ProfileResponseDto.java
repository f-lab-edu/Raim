package flab.project.dto;

import flab.project.domain.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long userId;
    private String phoneNumber;
    private String nickname;
    private String profileImage;
    private String backgroundImage;
    private String statusMessage;

    private ProfileResponseDto(Long userId, String phoneNumber, String nickname, String profileImage,
                               String backgroundImage, String statusMessage) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        this.statusMessage = statusMessage;
    }

    public static ProfileResponseDto of(User user) {
        return new ProfileResponseDto(user.getId(), user.getPhoneNumber(), user.getNickname(),
                user.getProfileImage(), user.getBackgroundImage(), user.getStatusMessage());
    }
}
