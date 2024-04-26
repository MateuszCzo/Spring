package mc.project.filmBase.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLockedRequest {
    private long id;
    private boolean accountNonLocked;
}
