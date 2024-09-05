package mc.project.online_store.dto.request;

import lombok.Data;
import mc.project.online_store.model.Delivery;
import mc.project.online_store.model.Payment;

import java.util.Set;

@Data
public class OrderRequest {
    private long paymentId;
    private long deliveryId;
    private long addressId;
    private long contactId;
    private Set<Long> productIds;
}
