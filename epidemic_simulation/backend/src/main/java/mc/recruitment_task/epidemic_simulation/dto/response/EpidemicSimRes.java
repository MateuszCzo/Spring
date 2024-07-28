package mc.recruitment_task.epidemic_simulation.dto.response;

import lombok.Data;

@Data
public class EpidemicSimRes {
    private int day;
    /**
     * Pi - the number of infected people
     */
    private int infected;
    /**
     * Pv - the number of healthy people susceptible to infection
     */
    private int healthy;
    /**
     * Pm - number of people who died
     */
    private int dead;
    /**
     * Pr - the number of people who have recovered and acquired immunity
     */
    private int immune;

    @Override
    public String toString() {
        return "EpidemicSimRes{" +
                "day=" + day +
                ", infected=" + infected +
                ", healthy=" + healthy +
                ", dead=" + dead +
                ", immune=" + immune +
                '}';
    }
}
