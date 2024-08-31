package mc.project.online_store.service.admin;

import mc.project.online_store.dto.request.DeliveryRequest;
import mc.project.online_store.dto.response.DeliveryResponse;

import java.util.List;

public interface DeliveryService {
    List<DeliveryResponse> getPage(String name, int page, int pageSize);

    DeliveryResponse getDelivery(long id);

    DeliveryResponse getDeliveryByOrderId(long orderId);

    DeliveryResponse postDelivery(DeliveryRequest request);

    DeliveryResponse putDelivery(long id, DeliveryRequest request);

    void deleteDelivery(long id);
}
