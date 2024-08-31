package mc.project.online_store.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String name;
    private String description;
    private boolean active;
    private String type;
}
