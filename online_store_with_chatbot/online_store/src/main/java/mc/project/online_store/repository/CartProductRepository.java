package mc.project.online_store.repository;

import mc.project.online_store.model.Cart;
import mc.project.online_store.model.CartProduct;
import mc.project.online_store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Set<CartProduct> findByCart(Cart cart);

    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);
}
