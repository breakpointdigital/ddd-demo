package pl.bpd.ddd.application.ticket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.bpd.ddd.application.shared.TemplateRenderer;
import pl.bpd.ddd.application.shared.events.DomainEventNotification;
import pl.bpd.ddd.application.shared.events.DomainEventNotificationHandler;
import pl.bpd.ddd.domain.ticket.event.TicketOpened;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyNewTicketEventHandler implements DomainEventNotificationHandler<TicketOpened> {
    private final JavaMailSender javaMailSender;
    private final TemplateRenderer thymeleafRenderer;
    private final ObjectMapper objectMapper;

    @Override
    @EventListener
    public void handle(DomainEventNotification<TicketOpened> notification) {
        var event = notification.source();
        try {
            log.info("sending email");
            var message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            var htmlBody = thymeleafRenderer.render("ticket-opened", Map.of(
                    "title", event.title(),
                    "assignee", objectMapper.convertValue(event.assignee(), Map.class),
                    "reporter", objectMapper.convertValue(event.reporter(), Map.class)
            ));
            messageHelper.setSubject("Ticket opened " + event.ticketId().id());
            messageHelper.setText(htmlBody, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("Cannot send email", e);
        }
    }
}
