package mc.project.online_store.dto.request;

import lombok.Data;
import mc.project.online_store.model.Delivery;
import mc.project.online_store.model.Payment;

import java.util.Set;

@Data
public class OrderRequest {
    private Payment payment;
    private Delivery delivery;
    private Set<Integer> productIds;
}
