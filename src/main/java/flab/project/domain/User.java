package flab.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String nickname;

    @Builder
    public User(String email, String password, String name, String phoneNumber, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }

}
