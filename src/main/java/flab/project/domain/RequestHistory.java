package flab.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class RequestHistory extends BaseEntity{
    @Id @GeneratedValue
    private Long id;

    private String sessionId;

    private LocalDateTime requestTime;

    @Builder
    public RequestHistory(String sessionId, LocalDateTime requestTime) {
        this.sessionId = sessionId;
        this.requestTime = requestTime;
    }

    public void updateRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }
}
