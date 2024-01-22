package pl.bpd.ddd.domain.member;

import pl.bpd.ddd.application.shared.CurrentUserInfo;
import pl.bpd.ddd.application.shared.UserRole;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findMemberById(String userId);

    Optional<Assignee> findAssigneeById(String userId);

    Optional<Reporter> findReporterById(String userId);

    Optional<Assignee> findRandomAssignee();

    default Member fromUserInfo(CurrentUserInfo currentUserInfo) {
        if (currentUserInfo.roles().contains(UserRole.TEAM)) {
            return new Assignee(currentUserInfo.id(), currentUserInfo.username(), currentUserInfo.email());
        } else if (currentUserInfo.roles().contains(UserRole.CUSTOMER)) {
            return new Reporter(currentUserInfo.id(), currentUserInfo.username(), currentUserInfo.email());
        }

        throw new RuntimeException("Invalid user role");
    }
}
