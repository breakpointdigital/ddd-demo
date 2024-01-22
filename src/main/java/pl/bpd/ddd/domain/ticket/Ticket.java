package pl.bpd.ddd.domain.ticket;

import com.github.f4b6a3.ulid.Ulid;
import jakarta.persistence.*;
import lombok.*;
import pl.bpd.ddd.domain.member.Assignee;
import pl.bpd.ddd.domain.member.AssigneeSelectorService;
import pl.bpd.ddd.domain.member.Member;
import pl.bpd.ddd.domain.member.Reporter;
import pl.bpd.ddd.domain.shared.DomainEntity;
import pl.bpd.ddd.domain.shared.ValidationResultException;
import pl.bpd.ddd.domain.shared.Validators;
import pl.bpd.ddd.domain.ticket.event.StatusHistoryEntryAdded;
import pl.bpd.ddd.domain.ticket.event.TicketOpened;
import pl.bpd.ddd.domain.ticket.event.TicketTitleUpdated;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.bpd.ddd.domain.shared.Validators.*;

@Getter
@Entity
@Table(name = "tickets")
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
public class Ticket extends DomainEntity {
    @EmbeddedId
    private TicketId id;
    private String title;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Reporter reporter;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "assignee_id", nullable = false)
    private Assignee assignee;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @ElementCollection
    @OrderBy("changeDate")
    @CollectionTable(name = "ticket_status_history", joinColumns = @JoinColumn(name = "ticket_id"))
    private List<StatusHistoryEntry> statusHistory = new ArrayList<>();
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    private List<ChecklistItem> checklist;
    /*
    A bit too long tip:
    In PostgreSQL, Instant (and OffsetDateTime) is mapped to column type "timestamp(6) with time zone".
    Under the hood, Instant keeps a datetime in the UTC timezone as a number of epoch-seconds and number of nanoseconds of a second.
    It may be a bit misleading at first glance, but in fact PostgreSQL doesn't have any date(time) type that stores a timezone:
    - timestamp with time zone (or psql-specific timestampz) - number of microseconds in the UTC timezone
    - timestamp without time zone (or just timestamp) - number of microseconds with an undefined timezone

    To manually insert a datetime into column, we need to use a specific format, e.g. '2023-12-10 12:43:00'.
    In this example, driver will convert the time from JVM timezone to UTC (e.g. in case of +01 timezone it would be 11:43:00).
    To indicate that we want to store datetime in the UTC timezone, we need to add Z timezone indicator at the end
    of the datetime string - '2023-12-10 12:43:00Z'.
    When storing Instant using JDBC, driver takes care of it.

    Conclusion - be careful when dealing with dates through JDBC, as a driver may automatically convert timezones.
     */
    private Instant createdDate;
    private Instant updatedDate;
    // Downside of using the same class for DB entity and domain entity. Deleted property is not a part of
    // the domain model, but there is no another place to put it.
    //private boolean deleted;

    public void changeStatus(TicketStatus newStatus, Member editor) {
        if (status.isClosed()) {
            throw new ValidationResultException("status", "already closed");
        }
        if (!status.canChangeTo(newStatus)) {
            throw new ValidationResultException("status", "transition not allowed");
        }

        validateNotNull(editor, "editor");

        var previousStatus = this.status;
        this.status = newStatus;
        var historyEntry = new StatusHistoryEntry(previousStatus, editor, Instant.now());
        statusHistory.add(historyEntry);

        addDomainEvent(new StatusHistoryEntryAdded(previousStatus, this.status, editor, historyEntry.changeDate()));
    }

    public void reassign(Assignee assignee) {
        if (status.isClosed()) {
            throw new ValidationResultException("status", "already closed");
        }

        validateNotNull(assignee, "assignee");

        this.assignee = assignee;
    }

    public void updateTitle(String title) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        validateNotBlank(title, "title");

        this.title = title.trim();
        addDomainEvent(new TicketTitleUpdated(id, title));
    }

    public void updateDescription(String description) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        validateNotBlank(description, "description");

        this.description = description.trim();
    }

    public static Ticket open(String title, String description, Reporter reporter, TicketIdGenerator ticketIdGenerator, AssigneeSelectorService assigneeSelectorService) {
        Validators.group()
                .notBlank(title, "title")
                .notBlank(description, "description")
                .notNull(reporter, "reporter")
                .throwIfNotValid();

        var assignee = assigneeSelectorService.select();
        if (assignee == null) {
            throw new IllegalStateException("Cannot assign null assignee");
        }

        Ticket ticket = new Ticket(
                ticketIdGenerator.generateNext(),
                title,
                description,
                reporter,
                assignee,
                TicketStatus.RECEIVED,
                new ArrayList<>(),
                new ArrayList<>(),
                Instant.now(),
                Instant.now()
        );
        ticket.addDomainEvent(new TicketOpened(ticket.id, ticket.title, ticket.assignee, ticket.reporter));
        return ticket;
    }

    public boolean isClosed() {
        return status == TicketStatus.CLOSED;
    }

    public List<StatusHistoryEntry> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }

    public List<ChecklistItem> getChecklist() {
        return Collections.unmodifiableList(checklist);
    }

    public Comment comment(Member author, String content) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        return Comment.create(getId(), author, content);
    }

    public void editComment(Comment comment, String content) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        comment.edit(content);
    }

    public void editChecklistItem(ChecklistItem.Id checklistItemId, String content) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        findChecklistItem(checklistItemId)
                .ifPresentOrElse(
                        checklistItem -> checklistItem.edit(content),
                        () -> {
                            throw new ValidationResultException("checklistItem", "not found");
                        }
                );
    }

    public void deleteChecklistItem(ChecklistItem.Id checklistItemId) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }
        boolean itemExists = this.checklist.stream().anyMatch(item -> item.getId().equals(checklistItemId));
        if (!itemExists) {
            throw new ValidationResultException("checklistItem", "not found");
        }

        this.checklist = this.checklist.stream()
                .filter(checklistItem -> !checklistItem.getId().equals(checklistItemId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void checkChecklistItem(ChecklistItem.Id checklistItemId) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        ChecklistItem item = findChecklistItem(checklistItemId)
                .orElseThrow(() -> new ValidationResultException("checklistItem", "not found"));
        item.check();
    }

    public void uncheckChecklistItem(ChecklistItem.Id checklistItemId) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }

        ChecklistItem item = findChecklistItem(checklistItemId)
                .orElseThrow(() -> new ValidationResultException("checklistItem", "not found"));
        item.uncheck();
    }

    private Optional<ChecklistItem> findChecklistItem(ChecklistItem.Id id) {
        return checklist.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public ChecklistItem.Id addChecklistItem(String content) {
        if (isClosed()) {
            throw new ValidationResultException("ticket", "is closed");
        }
        validateLt(checklist.size(), 10, "checklist", "max 10 checklist items are allowed");
        validateNotBlank(content, "content");

        var item = new ChecklistItem(
                new ChecklistItem.Id(Ulid.fast().toLowerCase()),
                id,
                content.trim(),
                false
        );

        checklist.add(item);

        return item.getId();
    }
}
