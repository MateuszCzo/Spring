package mc.project1.restapi.repository;

import mc.project1.restapi.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long>
{

}
