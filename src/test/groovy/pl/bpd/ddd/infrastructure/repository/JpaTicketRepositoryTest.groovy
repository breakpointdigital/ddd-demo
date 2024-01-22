package pl.bpd.ddd.infrastructure.repository

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import pl.bpd.ddd.domain.member.AssigneeSelectorService
import pl.bpd.ddd.domain.ticket.Ticket
import pl.bpd.ddd.domain.ticket.TicketId
import pl.bpd.ddd.domain.ticket.TicketIdGenerator
import spock.lang.Specification

@SpringBootTest
@Transactional
@Sql("/add-users.sql")
@Sql(scripts = "/ticket-repository-down.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class JpaTicketRepositoryTest extends Specification {
    @Autowired
    JpaMemberRepository memberRepository
    @Autowired
    JpaTicketRepository ticketRepository
    @Autowired
    AssigneeSelectorService assigneeSelectorService

    @SpringBean
    TicketIdGenerator ticketIdGenerator = Mock()

    def setup() {
        ticketIdGenerator.generateNext() >> new TicketId("proj-1")
    }

    def "should save ticket"() {
        given:
        def reporter = memberRepository.findReporterById("01H1APBFYS2VRNEND22YCJ94JC")
        def ticket = Ticket.open("new ticket", "new ticket desc", reporter.get(), ticketIdGenerator, assigneeSelectorService)

        ticket.addChecklistItem("new todo")
        ticket.addChecklistItem("buy milk")

        ticketRepository.save(ticket)

        when:
        def ticketFromDb = ticketRepository.findById(ticket.id).get()

        then:
        with(ticketFromDb) {
            title == "new ticket"
            description == "new ticket desc"
            checklist.size() == 2
            checklist.get(0).content == "new todo"
            checklist.get(1).content == "buy milk"
        }
    }
}
