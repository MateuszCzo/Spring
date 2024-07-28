package mc.recruitment_task.epidemic_simulation.enums;

import lombok.Getter;

@Getter
public enum EpidemicAlgorithmType {
    SIMPLE ("EpidemicAlgorithm"),
    ADVANCED ("AdvancedEpidemicAlgorithm");

    private final String serviceName;

    EpidemicAlgorithmType(String serviceName) {
        this.serviceName = serviceName;
    }
}
