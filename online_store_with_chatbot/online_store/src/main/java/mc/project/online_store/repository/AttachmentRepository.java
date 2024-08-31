package mc.project.online_store.repository;

import mc.project.online_store.model.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Page<Attachment> findByNameContaining(String name, Pageable pageable);

    Set<Attachment> findByIdIn(Collection<Integer> attachmentsIds);
}
