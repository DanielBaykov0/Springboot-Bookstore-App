package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.utils.PropertyVariables;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.EMAIL_SEND_FAILURE;

@Slf4j
@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final PropertyVariables propertyVariables;

    @Async
    @Transactional
    public void send(String subject, String receiver, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setFrom(propertyVariables.getFromEmail());
            log.info("Email send successfully with subject {}", subject);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send Email", e);
            throw new LibraryHTTPException(HttpStatus.INTERNAL_SERVER_ERROR, EMAIL_SEND_FAILURE);
        }
    }
}
