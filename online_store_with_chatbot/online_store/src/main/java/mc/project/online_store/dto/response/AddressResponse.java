package mc.project.online_store.dto.response;

import lombok.Data;
import mc.project.online_store.model.User;

@Data
public class AddressResponse {
    private Long id;
    private User user;
    private String street;
    private String number;
    private String postCode;
}
