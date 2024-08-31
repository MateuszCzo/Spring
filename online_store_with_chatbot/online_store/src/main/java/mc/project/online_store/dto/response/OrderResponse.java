package mc.project.online_store.dto.response;

import lombok.Data;
import mc.project.online_store.model.Delivery;
import mc.project.online_store.model.Payment;

@Data
public class OrderResponse {
    private long id;
    private Payment payment;
    private Delivery delivery;
}
