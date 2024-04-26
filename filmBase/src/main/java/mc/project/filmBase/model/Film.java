package mc.project.filmBase.model;

import jakarta.persistence.*;
import lombok.*;
import mc.project.filmBase.enums.FilmStatus;

import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String description;
    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Collection<Actor> actors;
    @OneToMany(mappedBy="film")
    private Collection<Rating> ratings;
    @Enumerated(EnumType.STRING)
    private FilmStatus status;
}
