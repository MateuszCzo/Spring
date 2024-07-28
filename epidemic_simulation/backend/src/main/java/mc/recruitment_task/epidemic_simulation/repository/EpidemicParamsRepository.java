package mc.recruitment_task.epidemic_simulation.repository;

import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpidemicParamsRepository extends JpaRepository<EpidemicParameters, Long> {
    boolean existsByName(String name);

    Page<EpidemicParameters> findByNameContaining(String name, Pageable page);

    long countByNameContaining(String name);

    List<EpidemicParameters> findByNameAndIdNot(String name, long id);
}
