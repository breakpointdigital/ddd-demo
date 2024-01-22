package pl.bpd.ddd.domain.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("TEAM")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignee extends Member {
    public Assignee(String userId, String username, String email) {
        super(userId, username, email);
    }
}
