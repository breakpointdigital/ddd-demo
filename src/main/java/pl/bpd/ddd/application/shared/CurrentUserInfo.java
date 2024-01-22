package pl.bpd.ddd.application.shared;

import java.util.Set;

public interface CurrentUserInfo {
    String id();

    String username();

    String email();

    Set<String> roles();
}
