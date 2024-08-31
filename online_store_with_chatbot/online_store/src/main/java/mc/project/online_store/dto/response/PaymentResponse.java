package mc.project.online_store.dto.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String type;
}
