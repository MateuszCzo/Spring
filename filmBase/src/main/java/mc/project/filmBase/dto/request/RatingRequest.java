package mc.project.filmBase.dto.request;

import lombok.*;
import mc.project.filmBase.enums.RatingStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    private long id;
    private String description;
    private int rating;
    private long filmId;
    private RatingStatus status;
}
