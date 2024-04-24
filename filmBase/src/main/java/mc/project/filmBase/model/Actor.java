package mc.project.filmBase.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstname;
    private String lastname;
    @ManyToMany(mappedBy = "actors")
    private Collection<Film> films = Collections.emptyList();
}
