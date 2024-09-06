package mc.project.online_store.repository;

import mc.project.online_store.model.Order;
import mc.project.online_store.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Set<OrderProduct> findByOrder(Order order);
}
