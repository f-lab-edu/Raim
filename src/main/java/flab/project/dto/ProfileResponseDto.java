package flab.project.dto;

import flab.project.domain.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private Long userId;
    private String phoneNumber;
    private String nickname;
    private String profileImageUrl;
    private String backgroundImageUrl;
    private String statusMessage;

    private ProfileResponseDto(Long userId, String phoneNumber, String nickname, String profileImageUrl,
                               String backgroundImageUrl, String statusMessage) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
        this.statusMessage = statusMessage;
    }

//    public static ProfileResponseDto of(User user) {
//        return new ProfileResponseDto(user.getId(), user.getPhoneNumber(), user.getNickname(),
//                user.getProfileImageUrl(), user.getBackgroundImageUrl(), user.getStatusMessage());
//    }
}
