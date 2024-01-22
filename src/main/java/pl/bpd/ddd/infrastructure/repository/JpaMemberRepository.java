package pl.bpd.ddd.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.bpd.ddd.domain.member.Assignee;
import pl.bpd.ddd.domain.member.Member;
import pl.bpd.ddd.domain.member.MemberRepository;
import pl.bpd.ddd.domain.member.Reporter;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {
    private final EntityManager entityManager;

    @Override
    public Optional<Member> findMemberById(String userId) {
        return Optional.ofNullable(entityManager.find(Member.class, userId));
    }

    @Override
    public Optional<Assignee> findAssigneeById(String userId) {
        return Optional.ofNullable(entityManager.find(Assignee.class, userId));
    }

    @Override
    public Optional<Reporter> findReporterById(String userId) {
        return Optional.ofNullable(entityManager.find(Reporter.class, userId));
    }

    @Override
    public Optional<Assignee> findRandomAssignee() {
        try {
            // not optimal query, but it's ok for demo purposes
            var assignee = entityManager.createQuery("select a from Assignee a order by RANDOM()", Assignee.class)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.of(assignee);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findMemberByUsername(String username) {
        try {
            var member = entityManager.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", username)
                    .setMaxResults(1)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
