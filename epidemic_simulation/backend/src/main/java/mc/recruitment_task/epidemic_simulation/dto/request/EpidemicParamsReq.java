package mc.recruitment_task.epidemic_simulation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import mc.recruitment_task.epidemic_simulation.enums.EpidemicAlgorithmType;
import mc.recruitment_task.epidemic_simulation.validator.object.DeathTimeAndRecoveryTimeInterface;
import mc.recruitment_task.epidemic_simulation.validator.object.InfectedAndPopulationInterface;
import mc.recruitment_task.epidemic_simulation.validator.request.DeathTimeLessThanRecoveryTime;
import mc.recruitment_task.epidemic_simulation.validator.request.InfectedLessThanPopulation;
import mc.recruitment_task.epidemic_simulation.validator.request.UniqueEpidemicName;

@Data
@InfectedLessThanPopulation
@DeathTimeLessThanRecoveryTime
public class EpidemicParamsReq implements DeathTimeAndRecoveryTimeInterface, InfectedAndPopulationInterface {
    @NotNull(message = "Name cannot be empty")
    @Size(min = 1, max = 256, message = "Name size must be between 1 and 256 characters")
    @UniqueEpidemicName()
    private String name;

    @Positive(message = "Population size must be greater than 0")
    private int population;

    @Positive(message = "Infected people must be greater than 0")
    private int infected;

    @Positive(message = "R indicator must be greater than 0")
    private float r;

    @Positive(message = "Mortality rate must be greater than 0")
    @Max(value = 1, message = "Mortality rate cannot be greater than 1")
    private float mortality;

    @Positive(message = "Recovery time must be greater than 0")
    private int recoveryTime;

    @Positive(message = "Death time must be greater than 0")
    private int deathTime;

    @Positive(message = "Simulation time must be greater than 0")
    private int simulationTime;

    @NotNull(message = "Algorithm type cannot be empty")
    private EpidemicAlgorithmType algorithmType;
}
