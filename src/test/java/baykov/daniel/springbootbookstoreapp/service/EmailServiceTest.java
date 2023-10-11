package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.utils.PropertyVariables;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private PropertyVariables propertyVariables;

    @Mock
    private MimeMessage mimeMessage;

    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(javaMailSender, propertyVariables);
    }

    @Test
    void testSendEmail_Success() throws MessagingException {
        String subject = "subject";
        String receiver = "recipient";
        String email = "body";
        String sender = "sender";

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        mimeMessageHelper.setText(email, true);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject(subject);
        when(propertyVariables.getFromEmail()).thenReturn(sender);

        emailService.send(subject, receiver, email);

        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}