package flab.project.service;

import flab.project.domain.User;
import flab.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registrationUser(User user) {
        userRepository.save(user);
    }

    public boolean duplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
