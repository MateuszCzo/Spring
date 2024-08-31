package mc.project.online_store.repository;

import mc.project.online_store.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByNameContaining(String name, Pageable pageable);
}
