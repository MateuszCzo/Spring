package mc.project.online_store.repository;

import mc.project.online_store.model.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Page<Delivery> findByNameContaining(String name, Pageable pageable);
}
