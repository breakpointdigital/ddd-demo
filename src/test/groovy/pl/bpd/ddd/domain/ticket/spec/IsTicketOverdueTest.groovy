package pl.bpd.ddd.domain.ticket.spec


import pl.bpd.ddd.domain.ticket.Ticket
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class IsTicketOverdueTest extends Specification {
    def "should satisfy overdue specification"() {
        given:
        def spec = new IsTicketOverdue(Clock.fixed(Instant.parse('2023-12-10T01:00:00.00Z'), ZoneOffset.UTC))

        expect:
        result == spec.isSatisfiedBy(ticket)

        where:
        ticket                                | result
        stubTicket('2022-12-10T01:00:00.00Z') | true
        stubTicket('2022-12-01T02:00:00.00Z') | true
        stubTicket('2023-12-03T00:59:59.00Z') | true
        stubTicket('2023-12-03T01:00:00.00Z') | false
        stubTicket('2023-12-05T01:00:00.00Z') | false
        stubTicket('2023-12-20T01:00:00.00Z') | false
    }

    def stubTicket(String createdDate) {
        def ticket = Stub(Ticket.class)
        ticket.getCreatedDate() >> Instant.parse(createdDate)
        // display more friendly test label
        ticket.toString() >> createdDate
        return ticket
    }
}
