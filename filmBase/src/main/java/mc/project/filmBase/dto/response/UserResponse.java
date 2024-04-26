package mc.project.filmBase.dto.response;

import lombok.*;
import mc.project.filmBase.enums.UserRole;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private long id;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private UserRole role;
}
