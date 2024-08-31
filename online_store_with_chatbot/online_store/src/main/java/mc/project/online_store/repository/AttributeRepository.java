package mc.project.online_store.repository;

import mc.project.online_store.model.Attribute;
import mc.project.online_store.model.AttributeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    Page<Attribute> findByAttributeType(AttributeType attributeType, Pageable pageable);

    List<Attribute> findByAttributeType(AttributeType attributeType);

    Set<Attribute> findByIdIn(Collection<Integer> attributesIds);
}
