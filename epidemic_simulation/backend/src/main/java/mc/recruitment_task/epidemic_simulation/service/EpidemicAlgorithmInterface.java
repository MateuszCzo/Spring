package mc.recruitment_task.epidemic_simulation.service;

import mc.recruitment_task.epidemic_simulation.model.EpidemicParameters;
import mc.recruitment_task.epidemic_simulation.model.EpidemicSimulation;

import java.util.List;

public interface EpidemicAlgorithmInterface {
    List<EpidemicSimulation> simulate(EpidemicParameters epidemicParameters);
}
