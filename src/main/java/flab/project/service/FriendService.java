package flab.project.service;

import flab.project.domain.Friend;
import flab.project.domain.User;
import flab.project.dto.FriendResponseDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.FriendRepository;
import flab.project.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public void plusFriendByPhoneNumber(User user, String phoneNumber) {
        User friend = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new KakaoException(ExceptionCode.USER_NOT_FOUND));

        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new KakaoException(ExceptionCode.ALREADY_FRIEND);
        }

        setFriend(user, friend);
    }

    @Transactional
    public void plusFriendByEmail(User user, String email) {
        User friend = userRepository.findByEmail(email).orElseThrow(() ->
                new KakaoException(ExceptionCode.USER_NOT_FOUND));

        // 이미 존재하는 친구사이인지 검증
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new KakaoException(ExceptionCode.ALREADY_FRIEND);
        }

        setFriend(user, friend);
    }

    private void setFriend(User user, User friend) {
        Friend entity = Friend.builder()
                .user(user)
                .friend(friend)
                .isBlock(false)
                .build();

        friendRepository.save(entity);
    }

    @Transactional
    public void deleteFriend(Long id) {
        Friend friend = friendRepository.findById(id).orElseThrow(() ->
                new KakaoException(ExceptionCode.NO_FRIEND_RELATIONSHIP));

        friendRepository.delete(friend);
    }

    @Transactional
    public void changeBlockFriend(Long id, boolean isBlock) {
        Friend friend = friendRepository.findById(id).orElseThrow(() ->
                new KakaoException(ExceptionCode.NO_FRIEND_RELATIONSHIP));

        friend.changeBlockMode(isBlock);
    }

    @Transactional
    public List<FriendResponseDto> getUserFriends(User user) {
        return friendRepository.findByUser(user).stream()
                .map(FriendResponseDto::of)
                .toList();
    }
}
