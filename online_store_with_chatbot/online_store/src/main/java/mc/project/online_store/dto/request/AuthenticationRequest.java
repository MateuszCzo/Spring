package mc.project.online_store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class AuthenticationRequest {
    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;
}
