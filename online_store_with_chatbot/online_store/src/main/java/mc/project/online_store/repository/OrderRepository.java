package mc.project.online_store.repository;

import mc.project.online_store.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    int countDistinctByDelivery(Delivery delivery);

    Page<Order> findByUser(User user, Pageable pageable);

    int countDistinctByPayment(Payment payment);

    int countDistinctByUser(User user);

    int countDistinctByProducts(Product product);
}
