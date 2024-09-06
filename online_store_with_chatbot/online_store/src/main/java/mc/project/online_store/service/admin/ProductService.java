package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.ProductRequest;
import mc.project.online_store.dto.response.OrderProductResponse;
import mc.project.online_store.dto.response.ProductResponse;
import mc.project.online_store.model.OrderProduct;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getPage(String name, int page, int pageSize);

    List<ProductResponse> getPageByCategoryId(long categoryId, int page, int pageSize);

    ProductResponse getProduct(long id);

    ProductResponse postProduct(ProductRequest request);

    ProductResponse putProduct(long id, ProductRequest request);

    void deleteProduct(long id);

    List<OrderProductResponse> getOrderProductList(long orderId);
}
