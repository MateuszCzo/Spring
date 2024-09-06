package mc.project.online_store.service.admin;

import mc.project.online_store.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getPage(int page, int pageSize);

    List<OrderResponse> getPageByUserId(long userId, int page, int pageSize);

    OrderResponse getOrder(long id);

    void deleteOrder(long id);
}
