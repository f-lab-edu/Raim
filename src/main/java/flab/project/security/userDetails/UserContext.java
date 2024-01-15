package flab.project.security.userDetails;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserContext extends User {

    private flab.project.domain.User user;

    public UserContext(flab.project.domain.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

    public flab.project.domain.User getUser() {
        return user;
    }
}
