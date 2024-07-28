package mc.recruitment_task.epidemic_simulation.service.impl;

import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpidemicAlgorithmTest {

    @InjectMocks
    private EpidemicAlgorithm epidemicAlgorithm;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void simulate() {
        int[] healthyPpl = {900, 866, 824, 768, 725};
        int[] infectedPpl = {100, 134, 166, 219, 168};
        int[] deadPpl = {0, 0, 10, 13, 17};
        int[] immunePpl = {0, 0, 0, 0, 90};
        EpidemicParameters request = EpidemicParameters.builder()
                .name("COVID-19")
                .population(1000)
                .infected(100)
                .r(1.3f)
                .mortality(0.1f)
                .recoveryTime(4)
                .deathTime(2)
                .simulationTime(5)
                .build();

        List<EpidemicSimulation> response = epidemicAlgorithm.simulate(request);

        assertEquals(response.size(), 5);

        for (int i = 0; i < 5; i++) {
            EpidemicSimulation day = response.get(i);

            assertEquals(healthyPpl[i], day.getHealthy());
            assertEquals(infectedPpl[i], day.getInfected());
            assertEquals(deadPpl[i], day.getDead());
            assertEquals(immunePpl[i], day.getImmune());
        }
    }
}