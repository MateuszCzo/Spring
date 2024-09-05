package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.DeliveryRequest;
import mc.project.online_store.dto.response.DeliveryResponse;
import mc.project.online_store.exception.RelationConflictException;
import mc.project.online_store.model.Delivery;
import mc.project.online_store.model.Order;
import mc.project.online_store.repository.DeliveryRepository;
import mc.project.online_store.repository.OrderRepository;
import mc.project.online_store.service.admin.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService, mc.project.online_store.service.front.DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<DeliveryResponse> getPage(String name, int page, int pageSize) {
        Page<Delivery> deliveries = deliveryRepository.findByNameContaining(
                name, PageRequest.of(page, pageSize));

        return deliveries
                .map(delivery -> objectMapper.convertValue(delivery, DeliveryResponse.class))
                .toList();
    }

    @Override
    public DeliveryResponse getDelivery(long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(delivery, DeliveryResponse.class);
    }

    @Override
    public DeliveryResponse getDeliveryByOrderId(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(order.getDelivery(), DeliveryResponse.class);
    }

    @Override
    public DeliveryResponse postDelivery(DeliveryRequest request) {
        Delivery delivery = objectMapper.convertValue(request, Delivery.class);

        deliveryRepository.save(delivery);

        return objectMapper.convertValue(delivery, DeliveryResponse.class);
    }

    @Override
    public DeliveryResponse putDelivery(long id, DeliveryRequest request) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        try {
            delivery = objectMapper.updateValue(delivery, request);
        } catch (JsonMappingException e) { }

        deliveryRepository.save(delivery);

        return objectMapper.convertValue(delivery, DeliveryResponse.class);
    }

    @Override
    public void deleteDelivery(long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        int orderCount = orderRepository.countDistinctByDelivery(delivery);

        if (orderCount > 0) {
            throw new RelationConflictException("Cannot delete delivery - delivery contain orders");
        }

        deliveryRepository.delete(delivery);
    }
}
