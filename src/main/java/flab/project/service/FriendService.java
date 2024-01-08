package flab.project.service;

import flab.project.domain.Friend;
import flab.project.domain.User;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.FriendRepository;
import flab.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void plusFriendByPhoneNumber(User user, String phoneNumber) {
        User friend = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() ->
                new KakaoException(ExceptionCode.USER_NOT_FOUND));

        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new KakaoException(ExceptionCode.ALREADY_FRIEND);
        }

        setFriend(user, friend);
    }

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
}
