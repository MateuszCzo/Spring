package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getPage(int page, int pageSize);

    List<OrderResponse> getPageByUserId(long userId, int page, int pageSize);

    OrderResponse getOrder(long id);

    OrderResponse putOrder(long id, OrderRequest request);

    void deleteOrder(long id);
}
