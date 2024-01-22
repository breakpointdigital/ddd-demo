package pl.bpd.ddd;

import pl.bpd.ddd.application.shared.CurrentUserInfo

record CurrentUserMock(String id, String username, String email, Set<String> roles) implements CurrentUserInfo {
}
