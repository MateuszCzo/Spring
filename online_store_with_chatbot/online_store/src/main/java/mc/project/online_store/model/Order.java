package mc.project.online_store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private Date date;
}
