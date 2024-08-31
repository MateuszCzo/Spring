package mc.project.online_store.repository;

import mc.project.online_store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Page<User> findByNameContaining(String name, Pageable pageable);
}
