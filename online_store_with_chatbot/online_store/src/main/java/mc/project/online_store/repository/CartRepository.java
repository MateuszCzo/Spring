package mc.project.online_store.repository;

import mc.project.online_store.model.Cart;
import mc.project.online_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
