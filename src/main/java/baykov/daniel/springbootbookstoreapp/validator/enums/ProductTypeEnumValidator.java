package baykov.daniel.springbootbookstoreapp.validator.enums;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ProductTypeEnumValidator implements ConstraintValidator<ProductTypeEnumValid, Product.ProductTypeEnum> {

    private Product.ProductTypeEnum[] productTypeEnums;

    @Override
    public void initialize(ProductTypeEnumValid constraint) {
        this.productTypeEnums = constraint.anyOf();
    }

    @Override
    public boolean isValid(Product.ProductTypeEnum value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(productTypeEnums).contains(value);
    }
}
