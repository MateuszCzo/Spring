package mc.project.filmBase.dto.request;

import lombok.*;
import mc.project.filmBase.enums.RatingStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingStatusRequest {
    private long id;
    private RatingStatus status;
}
