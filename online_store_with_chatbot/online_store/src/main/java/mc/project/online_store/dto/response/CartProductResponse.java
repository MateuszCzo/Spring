package mc.project.online_store.dto.response;

import lombok.Data;

@Data
public class CartProductResponse {
    private Long id;
    private ProductResponse productResponse;
    private int quantity;
}
