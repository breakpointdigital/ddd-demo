package pl.bpd.ddd.domain.ticket

import pl.bpd.ddd.domain.member.Assignee
import pl.bpd.ddd.domain.member.AssigneeSelectorService
import pl.bpd.ddd.domain.member.Reporter
import pl.bpd.ddd.domain.shared.ValidationResultException
import pl.bpd.ddd.domain.ticket.event.TicketOpened
import pl.bpd.ddd.domain.ticket.event.TicketTitleUpdated
import spock.lang.Specification

class TicketTest extends Specification {
    AssigneeSelectorService assigneeSelectorService = { new Assignee('assignee-id', 'assignee', 'assignee@mail.com') }

    def reporter = new Reporter("reporter-id", "reporter", "reporter@mail.com")

    TicketIdGenerator ticketIdGenerator = Mock()

    def setup() {
        ticketIdGenerator.generateNext() >> new TicketId("proj-1")
    }

    def "should create valid ticket"() {
        when:
        def ticket = Ticket.open("test title", "test description", reporter, ticketIdGenerator, assigneeSelectorService)

        then:
        ticket.getTitle() == "test title"
        ticket.getDescription() == "test description"
        ticket.getStatus() == TicketStatus.RECEIVED
        with(ticket.getReporter()) {
            userId() == "reporter-id"
            email() == "reporter@mail.com"
        }
        with(ticket.getAssignee()) {
            userId() == "assignee-id"
            email() == "assignee@mail.com"
        }
        ticket.getDomainEvents().any { it.class == TicketOpened }
    }

    def "should not create ticket with empty params"() {
        when:
        Ticket.open("", "", null, ticketIdGenerator, { null })

        then:
        def err = thrown(ValidationResultException)
        err.validationResult.errors.collect { it.key } ==~ ["title", "description", "reporter"]
    }

    def "should not create ticket when assignee selector returns null"() {
        when:
        Ticket.open("test", "test", reporter, ticketIdGenerator, { null })

        then:
        thrown(IllegalStateException)
    }

    def "should update title"() {
        given:
        def ticket = createTicket()

        when:
        ticket.updateTitle("new test title")

        then:
        ticket.getTitle() == "new test title"
        ticket.getDomainEvents().any { it.class == TicketTitleUpdated }
    }

    def "should not update title when ticket closed"() {
        given:
        def ticket = createTicket(TicketStatus.CLOSED)

        when:
        ticket.updateTitle("new test title")

        then:
        def err = thrown(ValidationResultException)
        err.validationResult.errors.any { it.key == "ticket" }
        ticket.getTitle() == "test title"
    }

    def "should update description"() {
        given:
        def ticket = createTicket()

        when:
        ticket.updateDescription("new test description")

        then:
        ticket.getDescription() == "new test description"
    }

    def "should not update description when ticket closed"() {
        given:
        def ticket = createTicket(TicketStatus.CLOSED)

        when:
        ticket.updateDescription("new test description")

        then:
        def err = thrown(ValidationResultException)
        err.validationResult.errors.any { it.key == "ticket" }
        ticket.getDescription() == "test description"
    }

    def "should update status"() {
        given:
        def ticket = createTicket()

        when:
        ticket.changeStatus(TicketStatus.VERIFIED, new Assignee("editor-id", "test editor", "editor@mail.com"))

        then:
        ticket.getStatus() == TicketStatus.VERIFIED
        ticket.getStatusHistory().size() == 1
        ticket.getStatusHistory().any { it.previousStatus() == TicketStatus.RECEIVED }
    }

    def "should add checklist item"() {
        given:
        def ticket = createTicket()

        when:
        ticket.addChecklistItem("test item")

        then:
        ticket.getChecklist().size() == 1
        ticket.getChecklist()[0].getContent() == "test item"
        !ticket.getChecklist()[0].isChecked()
    }

    def "should delete checklist item"() {
        given:
        def ticket = createTicket()
        def checklistItemId = ticket.addChecklistItem("test item")

        when:
        ticket.deleteChecklistItem(checklistItemId)

        then:
        ticket.getChecklist().size() == 0
    }

    def "should update checklist item content"() {
        given:
        def ticket = createTicket()
        def checklistItemId = ticket.addChecklistItem("test item")

        when:
        ticket.editChecklistItem(checklistItemId, "new test content")

        then:
        ticket.getChecklist().size() == 1
        ticket.getChecklist()[0].getContent() == "new test content"
    }

    def "should check and uncheck checklist item"() {
        given:
        def ticket = createTicket()
        def checklistItemId = ticket.addChecklistItem("test item")

        when:
        ticket.checkChecklistItem(checklistItemId)

        then:
        ticket.getChecklist().size() == 1
        ticket.getChecklist()[0].isChecked()

        when:
        ticket.uncheckChecklistItem(checklistItemId)

        then:
        ticket.getChecklist().size() == 1
        !ticket.getChecklist()[0].isChecked()
    }

    def "should create comment"() {
        given:
        def ticket = createTicket()

        when:
        def comment = ticket.comment(reporter, "test comment")

        then:
        noExceptionThrown()
        comment.getContent() == "test comment"
        comment.getCreatedAt() != null
        with(comment.getAuthor()) {
            userId() == "reporter-id"
            email() == "reporter@mail.com"
        }
    }

    def "should not create comment when ticket is closed"() {
        given:
        def ticket = createTicket(TicketStatus.CLOSED)

        when:
        ticket.comment(reporter, "test comment")

        then:
        def err = thrown(ValidationResultException)
        err.validationResult.errors.any { it.key == "ticket" }
    }

    def "should edit comment"() {
        given:
        def ticket = createTicket()
        def comment = ticket.comment(reporter, "test comment")

        when:
        ticket.editComment(comment, "new test comment")

        then:
        noExceptionThrown()
        comment.getContent() == "new test comment"
    }

    def "should not edit comment when ticket closed"() {
        given:
        def ticket = createTicket()
        def comment = ticket.comment(reporter, "test comment")

        when:
        ticket.changeStatus(TicketStatus.CLOSED, reporter)

        then:
        ticket.getStatus() == TicketStatus.CLOSED

        when:
        def editResult = ticket.editComment(comment, "new test comment")

        then:
        def err = thrown(ValidationResultException)
        err.validationResult.errors.any { it.key == "ticket" }
        comment.getContent() == "test comment"
    }

    private Ticket createTicket(TicketStatus ticketStatus = null) {
        def ticket = Ticket.open("test title", "test description", reporter, ticketIdGenerator, assigneeSelectorService)
        if (ticketStatus != null) {
            ticket.changeStatus(ticketStatus, reporter)
        }

        return ticket
    }
}
