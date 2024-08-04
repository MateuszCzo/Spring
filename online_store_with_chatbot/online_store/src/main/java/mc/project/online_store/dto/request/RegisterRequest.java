package mc.project.online_store.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import mc.project.online_store.validator.request.UniqueUsername;

@Data
public class RegisterRequest {
    @NotNull(message = "Username cannot be empty")
    @Size(min = 1, max = 256, message = "Username size must be between 1 and 256 characters")
    @Email(message = "Username must be a valid email")
    @UniqueUsername()
    private String username;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 5, max = 256, message = "Username size must be between 5 and 256 characters")
    private String password;
}
