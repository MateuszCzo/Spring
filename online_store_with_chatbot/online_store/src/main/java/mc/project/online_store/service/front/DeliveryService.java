package mc.project.online_store.service.front;

import mc.project.online_store.dto.response.DeliveryResponse;

import java.util.List;

public interface DeliveryService {
    List<DeliveryResponse> getPage(String name, int page, int pageSize);

    DeliveryResponse getDelivery(long id);
}
