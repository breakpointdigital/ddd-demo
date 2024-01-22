package pl.bpd.ddd.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.bpd.ddd.domain.member.Assignee;
import pl.bpd.ddd.domain.member.AssigneeSelectorService;
import pl.bpd.ddd.domain.member.MemberRepository;

@Primary
@Service
@RequiredArgsConstructor
public class RandomAssigneeSelectorService implements AssigneeSelectorService {
    private final MemberRepository memberRepository;

    @Override
    public Assignee select() {
        return memberRepository.findRandomAssignee().get();
    }
}
