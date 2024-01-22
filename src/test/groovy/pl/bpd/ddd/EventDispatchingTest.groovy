package pl.bpd.ddd

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import pl.bpd.ddd.application.comment.handler.AddInitialCommentEventHandler
import pl.bpd.ddd.application.shared.UserRole
import pl.bpd.ddd.application.shared.outbox.OutboxService
import pl.bpd.ddd.application.ticket.TicketService
import pl.bpd.ddd.application.ticket.handler.NotifyNewTicketEventHandler
import spock.lang.Specification

@SpringBootTest
@Sql("/add-users.sql")
@Sql(scripts = "/is-ticket-overdue-down.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(authorities = [UserRole.CUSTOMER])
class EventDispatchingTest extends Specification {
    @SpringBean
    NotifyNewTicketEventHandler notifyNewTicketEventHandler = Mock()

    @SpringBean
    AddInitialCommentEventHandler addInitialCommentEventHandler = Mock()

    @Autowired
    TicketService ticketService

    @Autowired
    OutboxService outboxService

    def "should publish and handle domain event and domain event notification"() {
        when:
        ticketService.openTicket("test title", "test description", new CurrentUserMock("01H1APBFYS2VRNEND22YCJ94JC", "test", "test@mail.com", Set.of(UserRole.CUSTOMER)))

        then:
        1 * addInitialCommentEventHandler.handle(_)
        0 * notifyNewTicketEventHandler.handle(_)

        when:
        outboxService.processPendingOutboxItems()

        then:
        0 * addInitialCommentEventHandler.handle(_)
        1 * notifyNewTicketEventHandler.handle(_)
    }
}
