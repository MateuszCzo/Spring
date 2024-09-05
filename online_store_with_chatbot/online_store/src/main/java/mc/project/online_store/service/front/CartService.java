package mc.project.online_store.service.front;

import mc.project.online_store.dto.request.CartProductRequest;

public interface CartService {
    void postProduct(CartProductRequest request);

    void deleteProduct(CartProductRequest request);
}
