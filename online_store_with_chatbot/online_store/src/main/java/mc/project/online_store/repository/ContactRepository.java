package mc.project.online_store.repository;

import mc.project.online_store.model.Contact;
import mc.project.online_store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByUser(User user, Pageable pageable);

    List<Contact> findByUser(User user);
}
