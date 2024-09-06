package mc.project.online_store.dto.request;

import lombok.Data;

@Data
public class OrderRequest {
    private long paymentId;
    private long deliveryId;
    private long addressId;
    private long contactId;
}
