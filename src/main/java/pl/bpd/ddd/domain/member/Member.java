package pl.bpd.ddd.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Member {
    @Id
    @Column(name = "id")
    private String userId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;

    public Member(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = "";
    }

    public String userId() {
        return userId;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    @Transient
    public String getRole() {
        return getClass().getAnnotation(DiscriminatorValue.class).value();
    }
}
