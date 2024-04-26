package mc.project.filmBase.model;

import jakarta.persistence.*;
import lombok.*;
import mc.project.filmBase.enums.RatingStatus;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private int rating;
    @ManyToOne
    @JoinColumn(name="film_id", nullable=false)
    private Film film;
    @Enumerated(EnumType.STRING)
    private RatingStatus status;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}
