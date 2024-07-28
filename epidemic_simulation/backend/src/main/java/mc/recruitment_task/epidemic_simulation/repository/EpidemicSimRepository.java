package mc.recruitment_task.epidemic_simulation.repository;

import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpidemicSimRepository extends JpaRepository<EpidemicSimulation, Long> {
    List<EpidemicSimulation> findAllByEpidemicParameters(EpidemicParameters params);

    void deleteByEpidemicParametersId(long id);
}
