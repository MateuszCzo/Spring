package mc.project.online_store.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_product")
public class OrderProduct implements ProductCalculationInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true)
    private int quantity;

    @Override
    public long getProductId() {
        return product.getId();
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public float getPrice() {
        return product.getPrice();
    }
}
