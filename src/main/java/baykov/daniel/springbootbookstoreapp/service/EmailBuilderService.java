package baykov.daniel.springbootbookstoreapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailBuilderService {

    private final TemplateEngine templateEngine;

    public String buildRegistrationEmail(String firstName, String lastName, String email, String confirmationUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("email", email);
        context.setVariable("link", confirmationUrl);
        return templateEngine.process("email-on-registration", context);
    }

    public String buildResetPasswordEmail(String firstName, String lastName, String resetUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("link", resetUrl);
        return templateEngine.process("email-forgot-password", context);
    }

    public String buildConfirmationEmail(String firstName, String lastName, String confirmationUrl) {
        Context context = new Context();
        context.setVariable("firstName", firstName);
        context.setVariable("lastName", lastName);
        context.setVariable("link", confirmationUrl);
        return templateEngine.process("email-confirmation-resend", context);
    }

//    public String buildNotificationEmail(User user, Book book, UserBookHistory userBookHistory) {
//        Context context = new Context();
//        context.setVariable("name", user.getFirstName());
//        context.setVariable("book", book.getTitle());
//        context.setVariable("returnDate", userBookHistory.getReturnDateTime());
//        return templateEngine.process("email-notification", context);
//    }
}
