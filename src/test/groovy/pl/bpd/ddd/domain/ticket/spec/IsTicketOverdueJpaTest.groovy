package pl.bpd.ddd.domain.ticket.spec

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.jdbc.Sql
import pl.bpd.ddd.infrastructure.config.FeignConfig
import pl.bpd.ddd.infrastructure.repository.TicketQueries
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

@DataJpaTest(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [FeignConfig]))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = ["/add-users.sql", "/is-ticket-overdue-up.sql"])
@Sql(scripts = "/is-ticket-overdue-down.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class IsTicketOverdueJpaTest extends Specification {
    @Autowired
    TicketQueries ticketQueries

    def "should find overdue tickets"() {
        given:
        def spec = new IsTicketOverdue(Clock.fixed(Instant.parse('2023-12-10T01:00:00.00Z'), ZoneOffset.UTC))

        when:
        def tickets = ticketQueries.findAll(spec.toQuery())

        then:
        tickets.size() == 3
    }

    def "should find not overdue tickets"() {
        given:
        def spec = new IsTicketOverdue(Clock.fixed(Instant.parse('2023-12-10T01:00:00.00Z'), ZoneOffset.UTC))

        when:
        def tickets = ticketQueries.findAll(spec.not().toQuery())

        then:
        tickets.size() == 3
    }
}
