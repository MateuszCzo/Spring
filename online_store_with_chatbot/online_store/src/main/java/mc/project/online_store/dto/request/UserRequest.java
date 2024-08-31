package mc.project.online_store.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
