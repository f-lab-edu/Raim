package flab.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<UserAgreement> userAgreements = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String phoneNumber, String nickname, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.userRole = UserRole.USER;
    }
}
