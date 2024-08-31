package mc.project.online_store.dto.response;

import lombok.Data;
import mc.project.online_store.enums.UserRole;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private UserRole role;
}
