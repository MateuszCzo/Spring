package mc.project.filmBase.dto.response;

import lombok.*;
import mc.project.filmBase.enums.RatingStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private long id;
    private String description;
    private int rating;
    private RatingStatus status;
}
