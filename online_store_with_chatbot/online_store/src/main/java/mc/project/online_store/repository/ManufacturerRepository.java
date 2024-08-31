package mc.project.online_store.repository;

import mc.project.online_store.model.Manufacturer;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Page<Manufacturer> findByNameContaining(String name, Pageable pageable);
}
