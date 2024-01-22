package pl.bpd.ddd.domain.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.bpd.ddd.domain.shared.EntityId;
import pl.bpd.ddd.domain.shared.Validators;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
public class TicketId implements EntityId {
    @Column(name = "ticket_id")
    private String id;

    public TicketId(String id) {
        Validators.validateRegex(id, ".*\\-\\d+", "ticketId");
        this.id = id;
    }

    public String getProjectName() {
        return id.split("-")[0];
    }

    public int getTicketNumber() {
        return Integer.parseInt(id.split("-")[1]);
    }

    @Override
    public String id() {
        return id;
    }
}
