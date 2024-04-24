package mc.project.filmBase.dto.request;

import lombok.*;
import mc.project.filmBase.enums.FilmStatus;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmRequest {
    private long id;
    private String title;
    private String description;
    private Collection<Long> actorIds;
    private FilmStatus status;
}
