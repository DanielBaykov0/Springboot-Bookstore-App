package baykov.daniel.springbootbookstoreapp.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${api.publishable.test.key}")
    private String stripePublishableTestKey;

    @Value("${api.secret.test.key}")
    private String stripeSecretTestKey;

    @Bean
    public void inItStripe() {
        Stripe.apiKey = stripeSecretTestKey;
    }
}
