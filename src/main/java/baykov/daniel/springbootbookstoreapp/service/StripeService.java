package baykov.daniel.springbootbookstoreapp.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeService {

    public String createPaymentIntent(BigDecimal amount) throws StripeException {
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCurrency("usd")
                .setAmount(amount.multiply(new BigDecimal(100)).longValue())
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);

        return intent.getId();
    }
}
