package mc.project.filmBase.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActorResponse {
    private long id;
    private String firstname;
    private String lastname;
}
