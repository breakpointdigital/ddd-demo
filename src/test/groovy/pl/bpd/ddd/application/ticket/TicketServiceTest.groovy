package pl.bpd.ddd.application.ticket

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import pl.bpd.ddd.CurrentUserMock
import pl.bpd.ddd.application.shared.UserRole
import pl.bpd.ddd.domain.ticket.CommentRepository
import pl.bpd.ddd.domain.ticket.TicketId
import pl.bpd.ddd.domain.ticket.TicketRepository
import spock.lang.Specification

@Transactional
@SpringBootTest
@Sql("/add-users.sql")
@Sql(scripts = ["/ticket-repository-down.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TicketServiceTest extends Specification {
    @Autowired
    TicketService ticketService
    @Autowired
    TicketRepository ticketRepository
    @Autowired
    CommentRepository commentRepository

    @WithMockUser(authorities = [UserRole.CUSTOMER])
    def "should create initial commit"() {
        when:
        def ticket = ticketService.openTicket("test title", "test desc", new CurrentUserMock("01H1APBFYS2VRNEND22YCJ94JC", "admin", "admin@mail.com", Set.of(UserRole.CUSTOMER)))
        def comments = commentRepository.findForTicket(new TicketId(ticket.id()), Pageable.unpaged());

        then:
        comments.size() == 1
    }
}
