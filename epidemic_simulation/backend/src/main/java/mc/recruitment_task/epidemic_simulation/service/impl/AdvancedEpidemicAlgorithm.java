package mc.recruitment_task.epidemic_simulation.service.impl;

import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("AdvancedEpidemicAlgorithm")
public class AdvancedEpidemicAlgorithm extends EpidemicAlgorithm {
    @Override
    public List<EpidemicSimulation> simulate(EpidemicParameters params) {
        int length = params.getSimulationTime();
        int[] newInfected = new int[length];
        int[] newDead = new int[length];

        int groupIndex = params.getRecoveryTime() - params.getDeathTime();

        float initialR = params.getR() /
                ((params.getMortality() * params.getDeathTime()) +
                        ((1 - params.getMortality()) * params.getRecoveryTime()));

        List<EpidemicSimulation> result = new ArrayList<>(length);

        EpidemicSimulation firstDay = EpidemicSimulation.builder()
                .day(0)
                .epidemicParameters(params)
                .infected(params.getInfected())
                .healthy(params.getPopulation() - params.getInfected())
                .build();
        result.add(firstDay);
        newInfected[0] = params.getInfected();
        EpidemicSimulation prevDay = firstDay;

        for (int i = 1; i < length; i++) {
            float r = initialR * ((float) prevDay.getHealthy() / params.getPopulation());

            int dead = calculateDead(params, prevDay, i, newInfected, newDead);
            int newImmune = calculateImmune(params, i, newInfected, newDead, groupIndex);
            int infected = calculateInfected(prevDay, i, newInfected, newDead, newImmune, r);
            int healthy = calculateHealthy(prevDay, i, newInfected);

            EpidemicSimulation currDay = EpidemicSimulation.builder()
                    .epidemicParameters(params)
                    .day(i)
                    .dead(dead)
                    .immune(prevDay.getImmune() + newImmune)
                    .infected(infected)
                    .healthy(healthy)
                    .build();

            result.add(currDay);
            prevDay = currDay;
        }

        return result;
    }
}
