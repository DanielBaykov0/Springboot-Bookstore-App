package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "`order`")
public class Order extends BaseEntity {

    @Transient
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_intent_id", nullable = false)
    private String paymentIntentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime modifiedOn;

    private LocalDateTime deliveredOn;

    private String comment;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contactPhoneNumber;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Getter
    public enum PaymentMethod {
        CARD,
        UPON_DELIVERY
    }

    @Getter
    public enum OrderStatusEnum {
        NEW,
        CANCELED,
        CHECKOUT,
        WAITING_FOR_SHIPMENT,
        SHIPMENT,
        COMPLETED,
        REFUND,
    }
}
