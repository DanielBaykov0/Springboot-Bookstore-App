package baykov.daniel.springbootbookstoreapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity {

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Map<Product, CartItem> products = new LinkedHashMap<>();

    private BigDecimal productPrice;

    private Long productsCount = 0L;

    private BigDecimal productsSum = BigDecimal.ZERO;

    public int getProductQuantity(Product product) {
        CartItem cartItem = this.getProducts().get(product);
        return cartItem != null ? cartItem.getQuantity() : 0;
    }

    public void addProduct(Product product, int quantity) {
        CartItem cartItem = this.getProducts().get(product);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            this.getProducts().put(product, cartItem);
        }
        updateProductsSum(product.getPrice().multiply(BigDecimal.valueOf(quantity)), quantity);
    }

    public void removeProduct(Product product, int quantity) {
        CartItem cartItem = this.getProducts().get(product);
        if (cartItem != null) {
            int updatedQuantity = cartItem.getQuantity() - quantity;
            if (updatedQuantity <= 0) {
                this.getProducts().remove(product);
            } else {
                cartItem.setQuantity(updatedQuantity);
            }
            updateProductsSum(product.getPrice().multiply(BigDecimal.valueOf(-quantity)), -quantity);
        }
    }

    public void updateProductsSum(BigDecimal priceChange, int quantityChange) {
        this.setProductsSum(this.getProductsSum().add(priceChange));
        this.setProductsCount(this.getProductsCount() + quantityChange);
    }
}
