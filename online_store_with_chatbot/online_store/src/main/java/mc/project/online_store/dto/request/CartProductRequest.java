package mc.project.online_store.dto.request;

import lombok.Data;

@Data
public class CartProductRequest {
    private long productId;
    private int quantity;
}
