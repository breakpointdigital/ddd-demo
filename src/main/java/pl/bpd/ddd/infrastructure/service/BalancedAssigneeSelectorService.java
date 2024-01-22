package pl.bpd.ddd.infrastructure.service;

import org.springframework.stereotype.Service;
import pl.bpd.ddd.domain.member.Assignee;
import pl.bpd.ddd.domain.member.AssigneeSelectorService;

// not implemented yet, just for demo purposes
@Service
public class BalancedAssigneeSelectorService implements AssigneeSelectorService {
    @Override
    public Assignee select() {
        return null;
    }
}
