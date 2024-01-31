package flab.project.service;

import flab.project.domain.Friend;
import flab.project.domain.FriendStatus;
import flab.project.domain.User;
import flab.project.dto.FriendResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.FriendRepository;
import flab.project.repository.FriendRepositoryCustom;
import flab.project.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendRepositoryCustom friendRepositoryCustom;

    @Transactional
    public FriendResponseDto plusFriendByPhoneNumber(User user, String phoneNumber) {
        User friendUser = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new KakaoException(ExceptionCode.USER_NOT_FOUND));

        checkAlreadyFriend(user, friendUser);

        Friend friend = setFriend(user, friendUser);

        return FriendResponseDto.of(friend);
    }

    @Transactional
    public FriendResponseDto plusFriendByEmail(User user, String email) {
        User friendUser = userRepository.findByEmail(email).orElseThrow(() ->
                new KakaoException(ExceptionCode.USER_NOT_FOUND));

        // 이미 존재하는 친구사이인지 검증
        checkAlreadyFriend(user, friendUser);

        Friend friend = setFriend(user, friendUser);

        return FriendResponseDto.of(friend);
    }

    private void checkAlreadyFriend(User user, User friendUser) {
        if (friendRepository.existsByUserAndFriend(user, friendUser)) {
            throw new KakaoException(ExceptionCode.ALREADY_FRIEND);
        }
    }

    private Friend setFriend(User user, User friendUser) {
        Friend friend = Friend.builder()
                .user(user)
                .friend(friendUser)
                .friendStatus(FriendStatus.ACTIVE)
                .build();

        // 메시지 큐를 연결해서 나를 추가한 친구에 저장하거나 푸시 메시지를 보낸다.

        friendRepository.save(friend);

        return friend;
    }

    @Transactional
    public FriendResponseDto changeFriendMode(String userEmail, Long friendId, FriendStatus friendMode) {
        User loginUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));

        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new KakaoException(ExceptionCode.NO_FRIEND_RELATIONSHIP));

        checkUserFriend(loginUser, friend);

        friend.changeFriendMode(friendMode);

        friendRepository.save(friend);

        return FriendResponseDto.of(friend);
    }

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getUserFriends(String userEmail, FriendStatus friendStatus) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new KakaoException(ExceptionCode.USER_NOT_FOUND));

        switch (friendStatus) {
            case ACTIVE -> {
                return getUserActiveFriends(user);
            }
            case HIDDEN, BLOCK -> {
                return getUserFriends(user, friendStatus);
            }
            default -> throw new KakaoException(ExceptionCode.BAD_REQUEST);
        }
    }

    private List<FriendResponseDto> getUserActiveFriends(User user) {
        return friendRepository.findByUser(user).stream()
                .map(FriendResponseDto::of)
                .toList();
    }

    private List<FriendResponseDto> getUserFriends(User user, FriendStatus friendStatus) {
        return friendRepositoryCustom.findFriend(friendStatus).stream()
                .map(FriendResponseDto::of)
                .toList();
    }


    private void checkUserFriend(User loginUser, Friend friend) {

        // User의 친구 목록에 해당 친구 id가 있는지 확인한다.
        Set<Long> friends = loginUser.getFriends().stream().map(Friend::getId).collect(Collectors.toSet());

        if (!friends.contains(friend.getId())) {
            throw new KakaoException(ExceptionCode.NOT_FRIEND);
        }
/*
        // User의 id가 해당 Friend의 user로 등록되어있는지 확인한다.
        if (loginUser.getId() != friend.getUser().getId()) {
            throw new KakaoException(ExceptionCode.NOT_FRIEND);
        }
*/
    }
}
