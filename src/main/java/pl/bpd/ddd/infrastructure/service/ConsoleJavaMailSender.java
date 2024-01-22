package pl.bpd.ddd.infrastructure.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
public class ConsoleJavaMailSender extends JavaMailSenderImpl {
    @Override
    protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
        for (var mimeMessage : mimeMessages) {
            try {
                log.info("Sending email {}\n{}", mimeMessage.getSubject(), mimeMessage.getContent());
            } catch (Exception e) {
                log.error("Cannot log email", e);
            }
        }
    }
}
