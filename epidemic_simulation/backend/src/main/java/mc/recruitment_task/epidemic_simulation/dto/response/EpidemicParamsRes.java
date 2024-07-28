package mc.recruitment_task.epidemic_simulation.dto.response;

import lombok.Data;

@Data
public class EpidemicParamsRes {
    private long id;
    private String name;
    private int population;
    private int infected;
    private float r;
    private float mortality;
    private int recoveryTime;
    private int deathTime;
    private int simulationTime;
}