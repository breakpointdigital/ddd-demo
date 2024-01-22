package pl.bpd.ddd.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.bpd.ddd.domain.member.Member;

public interface MemberQueries extends JpaRepository<Member, String> {
}
