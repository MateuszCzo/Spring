package mc.project.online_store.repository;

import mc.project.online_store.model.AttributeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeTypeRepository extends JpaRepository<AttributeType, Long> {
    Page<AttributeType> findByNameContaining(String name, Pageable pageable);
}
