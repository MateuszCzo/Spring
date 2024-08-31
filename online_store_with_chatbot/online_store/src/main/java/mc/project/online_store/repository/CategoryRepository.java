package mc.project.online_store.repository;

import mc.project.online_store.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByNameContaining(String name, Pageable pageable);

    int countDistinctByParent(Category category);
}
