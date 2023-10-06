package baykov.daniel.springbootbookstoreapp.validator.enums;

import baykov.daniel.springbootbookstoreapp.entity.Order;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PaymentMethodEnumValidator implements ConstraintValidator<PaymentMethodEnumValid, Order.PaymentMethod> {

    private Order.PaymentMethod[] paymentMethods;

    @Override
    public void initialize(PaymentMethodEnumValid constraint) {
        this.paymentMethods = constraint.anyOf();
    }

    @Override
    public boolean isValid(Order.PaymentMethod value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(paymentMethods).contains(value);
    }
}
