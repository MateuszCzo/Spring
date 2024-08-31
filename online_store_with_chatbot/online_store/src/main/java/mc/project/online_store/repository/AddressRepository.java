package mc.project.online_store.repository;

import mc.project.online_store.model.Address;
import mc.project.online_store.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByUser(User user, Pageable pageable);

    List<Address> findByUser(User user);
}
