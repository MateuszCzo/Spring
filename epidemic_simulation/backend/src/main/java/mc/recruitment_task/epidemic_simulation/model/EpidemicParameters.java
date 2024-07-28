package mc.recruitment_task.epidemic_simulation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpidemicParameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int population;

    @Column(nullable = false)
    private int infected;

    @Column(nullable = false)
    private float r;

    @Column(nullable = false)
    private float mortality;

    @Column(nullable = false)
    private int recoveryTime;

    @Column(nullable = false)
    private int deathTime;

    @Column(nullable = false)
    private int simulationTime;

    @Override
    public String toString() {
        return "EpidemicParameters{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", population=" + population +
                ", infected=" + infected +
                ", rIndicator=" + r +
                ", mortality=" + mortality +
                ", recoveryTime=" + recoveryTime +
                ", deathTime=" + deathTime +
                ", simulationTime=" + simulationTime +
                '}';
    }
}
