package pl.bpd.ddd.domain.ticket;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import pl.bpd.ddd.domain.shared.EntityId;
import pl.bpd.ddd.domain.shared.ValidationResultException;

@Entity
@Getter
@Table(name = "checklist")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
public class ChecklistItem {
    @EmbeddedId
    @EqualsAndHashCode.Include
    private Id id;
    private TicketId ticketId;
    private String content;
    private boolean checked;

    protected void edit(String content) {
        if (StringUtils.isBlank(content)) {
            throw new ValidationResultException("content", "must not be blank");
        }
        this.content = content.trim();
    }

    protected void check() {
        this.checked = true;
    }

    protected void uncheck() {
        this.checked = false;
    }

    @Embeddable
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED) // for JPA
    static public class Id implements EntityId {
        @Column(name = "checklist_item_id")
        private String id;

        @Override
        public String id() {
            return id;
        }
    }
}
