package pl.bpd.ddd.application.shared;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserRole {
    public static final String PREFIX = "ROLE_";
    public static final String TEAM = PREFIX + "TEAM";
    public static final String CUSTOMER = PREFIX + "CUSTOMER";
}
