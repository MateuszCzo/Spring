package mc.project.online_store.repository;

import mc.project.online_store.model.Category;
import mc.project.online_store.model.Image;
import mc.project.online_store.model.Manufacturer;
import mc.project.online_store.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    int countDistinctByCategory(Category category);

    int countDistinctByManufacturer(Manufacturer manufacturer);

    Set<Product> findByIdIn(Collection<Long> productIds);

    Page<Product> findByNameContaining(String name, Pageable pageable);

    Page<Product> findByCategory(Category category, Pageable pageable);

    int countDistinctByImage(Image image);
}
