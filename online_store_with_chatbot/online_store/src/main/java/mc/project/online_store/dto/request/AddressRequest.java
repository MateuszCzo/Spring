package mc.project.online_store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequest {
    @NotNull(message = "Street cannot be empty")
    private String street;
    @NotNull(message = "Number cannot be empty")
    private String number;
    @NotNull(message = "PostCode cannot be empty")
    private String postCode;
}
