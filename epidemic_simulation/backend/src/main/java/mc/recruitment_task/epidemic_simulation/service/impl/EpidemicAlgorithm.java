package mc.recruitment_task.epidemic_simulation.service.impl;

import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;
import mc.recruitment_task.epidemic_simulation.service.EpidemicAlgorithmInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("EpidemicAlgorithm")
public class EpidemicAlgorithm implements EpidemicAlgorithmInterface {
    /**
     * O(n) - time & space (n - number of days)
     */
    @Override
    public List<EpidemicSimulation> simulate(EpidemicParameters parameters) {
        int length = parameters.getSimulationTime();
        int[] newInfected = new int[length];
        int[] newDead = new int[length];
        // index of a group of the same people
        int groupIndex = parameters.getRecoveryTime() - parameters.getDeathTime();

        // the number of people infected per day by a sick person during the illness
        float r = parameters.getR() /
                ((parameters.getMortality() * parameters.getDeathTime()) +
                        ((1 - parameters.getMortality()) * parameters.getRecoveryTime()));

        List<EpidemicSimulation> result = new ArrayList<>(length);

        // first day
        EpidemicSimulation firstDay = EpidemicSimulation.builder()
                .day(0)
                .epidemicParameters(parameters)
                .infected(parameters.getInfected())
                .healthy(parameters.getPopulation() - parameters.getInfected())
                .build();
        result.add(firstDay);
        newInfected[0] = parameters.getInfected();
        EpidemicSimulation prevDay = firstDay;

        // next days
        for (int i = 1; i < length; i++) {
            int dead = calculateDead(parameters, prevDay, i, newInfected, newDead);
            int newImmune = calculateImmune(parameters, i, newInfected, newDead, groupIndex);
            int infected = calculateInfected(prevDay, i, newInfected, newDead, newImmune, r);
            int healthy = calculateHealthy(prevDay, i, newInfected);

            EpidemicSimulation currDay = EpidemicSimulation.builder()
                    .epidemicParameters(parameters)
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

    protected int calculateDead(EpidemicParameters parameters, EpidemicSimulation prevDay,
                                int i, int[] newInfected, int[] newDead) {
        int infectedIndex = i - parameters.getDeathTime();
        if (infectedIndex >= 0) {
            newDead[i] = Math.round(newInfected[infectedIndex] * parameters.getMortality());
        }
        return prevDay.getDead() + newDead[i];
    }

    protected int calculateImmune(EpidemicParameters parameters,
                                  int i, int[] newInfected, int[] newDead, int groupIndex) {
        int infectedIndex = i - parameters.getRecoveryTime();
        int deadIndex = i - groupIndex;
        int newImmune = 0;
        if (infectedIndex >= 0) {
            newImmune = newInfected[infectedIndex] - newDead[deadIndex];
        }
        return newImmune;
    }

    protected int calculateInfected(EpidemicSimulation prevDay,
                                    int i, int[] newInfected, int[] newDead, int newImmune, float r) {
        int currInfected = prevDay.getInfected() - newDead[i] - newImmune;
        newInfected[i] = Math.min(Math.round(currInfected * r), prevDay.getHealthy());
        return currInfected + newInfected[i];
    }

    protected int calculateHealthy(EpidemicSimulation prevDay, int i, int[] newInfected) {
        return prevDay.getHealthy() - newInfected[i];
    }
}
