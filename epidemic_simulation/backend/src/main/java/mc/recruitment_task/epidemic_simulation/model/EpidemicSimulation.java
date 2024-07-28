package mc.recruitment_task.epidemic_simulation.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpidemicSimulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="epidemic_parameters_id", nullable=false)
    private EpidemicParameters epidemicParameters;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int infected;

    @Column(nullable = false)
    private int healthy;

    @Column(nullable = false)
    private int dead;

    @Column(nullable = false)
    private int immune;

    @Override
    public String toString() {
        return "EpidemicSimulation{" +
                "id=" + id +
                ", day=" + day +
                ", infected=" + infected +
                ", healthy=" + healthy +
                ", dead=" + dead +
                ", immune=" + immune +
                '}';
    }
}
