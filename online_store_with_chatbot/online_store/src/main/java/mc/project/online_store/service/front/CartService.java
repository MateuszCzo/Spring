package mc.project.online_store.service.front;

import mc.project.online_store.dto.request.CartProductRequest;
import mc.project.online_store.dto.response.CartProductResponse;

public interface CartService {
    CartProductResponse postProduct(CartProductRequest request);

    CartProductResponse putProduct(CartProductRequest request);

    void deleteProduct(long id);
}
