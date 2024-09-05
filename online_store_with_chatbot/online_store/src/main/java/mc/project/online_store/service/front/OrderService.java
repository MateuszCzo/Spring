package mc.project.online_store.service.front;

import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getUserPage(int page, int pageSize);

    OrderResponse getUserOrder(long id);

    OrderResponse postUserOrder(OrderRequest request);
}
