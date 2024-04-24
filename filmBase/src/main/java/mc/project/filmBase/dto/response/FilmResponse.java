package mc.project.filmBase.dto.response;

import lombok.*;
import mc.project.filmBase.enums.FilmStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmResponse {
    private long id;
    private String title;
    private String description;
    private FilmStatus status;
}
