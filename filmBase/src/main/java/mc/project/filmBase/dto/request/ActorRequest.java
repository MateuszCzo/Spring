package mc.project.filmBase.dto.request;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActorRequest {
    private long id;
    private String firstname;
    private String lastname;
    private Collection<Long> filmIds;
}
