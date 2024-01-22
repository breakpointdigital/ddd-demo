package pl.bpd.ddd.domain.member;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CUSTOMER")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reporter extends Member {
    public Reporter(String userId, String username, String email) {
        super(userId, username, email);
    }
}
