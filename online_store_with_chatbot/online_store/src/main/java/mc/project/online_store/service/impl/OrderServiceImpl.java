package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.model.Order;
import mc.project.online_store.model.Product;
import mc.project.online_store.model.User;
import mc.project.online_store.repository.OrderRepository;
import mc.project.online_store.repository.ProductRepository;
import mc.project.online_store.repository.UserRepository;
import mc.project.online_store.service.admin.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<OrderResponse> getPage(int page, int pageSize) {
        Page<Order> orders = orderRepository.findAll(
                PageRequest.of(page, pageSize));

        return orders
                .map(order -> objectMapper.convertValue(order, OrderResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponse> getPageByUserId(long userId, int page, int pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Page<Order> orders = orderRepository.findByUser(
                user, PageRequest.of(page, pageSize));

        return orders
                .map(order -> objectMapper.convertValue(order, OrderResponse.class))
                .toList();
    }

    @Override
    public OrderResponse getOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(order, OrderResponse.class);
    }

    @Override
    @Transactional
    public OrderResponse putOrder(long id, OrderRequest request) {
        Set<Product> products = productRepository.findByIdIn(request.getProductIds());

        Order order = orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        float price = (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        try {
            order = objectMapper.updateValue(order, request);
        } catch (JsonMappingException e) { }
        order.setProducts(products);
        order.setPrice(price);

        orderRepository.save(order);

        return objectMapper.convertValue(order, OrderResponse.class);
    }

    @Override
    public void deleteOrder(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        orderRepository.delete(order);
    }
}
