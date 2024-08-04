package mc.project.online_store.dto.response;

import lombok.*;

@Data
public class RegisterResponse {
    private String token;
    private boolean registered;
}
