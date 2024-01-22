package pl.bpd.ddd.application.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bpd.ddd.application.shared.CurrentUserInfo;
import pl.bpd.ddd.application.shared.UserRole;
import pl.bpd.ddd.application.shared.exceptions.EntityNotFoundException;
import pl.bpd.ddd.application.ticket.dto.OpenTicketOutput;
import pl.bpd.ddd.application.ticket.dto.TicketDetailsOutput;
import pl.bpd.ddd.application.ticket.dto.TicketSummaryOutput;
import pl.bpd.ddd.domain.member.*;
import pl.bpd.ddd.domain.ticket.*;
import pl.bpd.ddd.domain.ticket.spec.IsTicketOverdue;

/**
 * Application service
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final AssigneeSelectorService assigneeSelectorService;
    private final TicketIdGenerator ticketIdGenerator;
    private final TicketMappers ticketMappers;

    @Secured(UserRole.CUSTOMER)
    public OpenTicketOutput openTicket(String title, String description, CurrentUserInfo currentUserInfo) {
        Reporter reporter = memberRepository.findReporterById(currentUserInfo.id())
                .orElseThrow(() -> new EntityNotFoundException("user not found"));
        var ticket = Ticket.open(title, description, reporter, ticketIdGenerator, assigneeSelectorService);
        ticketRepository.save(ticket);
        return new OpenTicketOutput(ticket.getId().id(), ticket.getTitle(), ticket.getAssignee(), ticket.getReporter());
    }

    public TicketDetailsOutput getTicket(String ticketId) {
        var ticket = findTicketOrThrow(ticketId);
        return ticketMappers.ticketToDetails(ticket);
    }

    @Secured(UserRole.TEAM)
    public void changeStatus(String ticketId, TicketStatus newStatus, CurrentUserInfo currentUserInfo) {
        Ticket ticket = findTicketOrThrow(ticketId);

        Member member = memberRepository.findMemberById(currentUserInfo.id())
                .orElseThrow(() -> new EntityNotFoundException("user not found"));
        ticket.changeStatus(newStatus, member);
        ticketRepository.save(ticket);
    }

    @Secured(UserRole.TEAM)
    public void editTitle(String ticketId, String title) {
        Ticket ticket = findTicketOrThrow(ticketId);

        ticket.updateTitle(title);
        ticketRepository.save(ticket);
    }

    @Secured(UserRole.TEAM)
    public void editDescription(String ticketId, String description) {
        Ticket ticket = findTicketOrThrow(ticketId);

        ticket.updateDescription(description);
        ticketRepository.save(ticket);
    }

    @Secured(UserRole.TEAM)
    public void reassign(String ticketId, String userId) {
        Ticket ticket = findTicketOrThrow(ticketId);

        Assignee assignee = memberRepository.findAssigneeById(userId)
                .orElseThrow(() -> new EntityNotFoundException("assignee not found"));
        ticket.reassign(assignee);
        ticketRepository.save(ticket);
    }

    @Secured(UserRole.TEAM)
    public Page<TicketSummaryOutput> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable)
                .map(ticketMappers::ticketToSummary);
    }

    @Secured(UserRole.TEAM)
    public Page<TicketSummaryOutput> getOverdueTickets(Pageable pageable) {
        return ticketRepository.findAll(new IsTicketOverdue(), pageable)
                .map(ticketMappers::ticketToSummary);
    }

    @Secured(UserRole.TEAM)
    public ChecklistItem.Id addChecklistItem(String ticketId, String content) {
        Ticket ticket = findTicketOrThrow(ticketId);

        var checklistItemId = ticket.addChecklistItem(content);
        ticketRepository.save(ticket);
        return checklistItemId;
    }

    @Secured(UserRole.TEAM)
    public void editChecklistItem(String ticketId, String checklistItemId, String content) {
        Ticket ticket = findTicketOrThrow(ticketId);

        ticket.editChecklistItem(new ChecklistItem.Id(checklistItemId), content);
        ticketRepository.save(ticket);
    }

    @Secured(UserRole.TEAM)
    public void deleteChecklistItem(String ticketId, String checklistItemId) {
        Ticket ticket = findTicketOrThrow(ticketId);

        ticket.deleteChecklistItem(new ChecklistItem.Id(checklistItemId));
        ticketRepository.save(ticket);
    }

    @Secured(UserRole.TEAM)
    public void checkChecklistItem(String ticketId, String checklistItemId, boolean checked) {
        Ticket ticket = findTicketOrThrow(ticketId);

        if (checked) {
            ticket.checkChecklistItem(new ChecklistItem.Id(checklistItemId));
            ticketRepository.save(ticket);
        } else {
            ticket.uncheckChecklistItem(new ChecklistItem.Id(checklistItemId));
            ticketRepository.save(ticket);
        }
    }

    private Ticket findTicketOrThrow(String ticketId) {
        return ticketRepository.findById(new TicketId(ticketId))
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }
}
