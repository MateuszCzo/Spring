package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getPage(String name, int page, int pageSize);

    List<ProductResponse> getPageByCategoryId(long categoryId, int page, int pageSize);

    ProductResponse getProduct(long id);

    List<ProductResponse> getUserOrderProduct(long orderId);

    List<ProductResponse> getUserCartProduct();
}