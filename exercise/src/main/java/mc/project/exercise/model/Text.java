package mc.project.exercise.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_text")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Text {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        private String content;
}
