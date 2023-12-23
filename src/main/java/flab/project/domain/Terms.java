package flab.project.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Terms {
    public final static long ESSENTIAL_TERMS_1 = 1;
    public final static long ESSENTIAL_TERMS_2 = 2;
    public final static long OPTIONAL_LOCATION_TERMS = 3;

    @Id
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @OneToMany(mappedBy = "terms")
    private List<UserAgreement> userAgreements = new ArrayList<>();

}
