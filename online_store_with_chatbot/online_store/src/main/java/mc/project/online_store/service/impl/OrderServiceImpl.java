package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.admin.OrderService;
import mc.project.online_store.service.auth.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService, mc.project.online_store.service.front.OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Override
    public List<OrderResponse> getPage(int page, int pageSize) {
        Page<Order> orders = orderRepository.findAll(
                PageRequest.of(page, pageSize));

        return orders
                .map(order -> objectMapper.convertValue(order, OrderResponse.class))
                .toList();
    }

    public List<OrderResponse> getPageByUser(User user, int page, int pageSize) {
        Page<Order> orders = orderRepository.findByUser(
                user, PageRequest.of(page, pageSize));

        return orders
                .map(order -> objectMapper.convertValue(order, OrderResponse.class))
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponse> getPageByUserId(long userId, int page, int pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

       return getPageByUser(user, page, pageSize);
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

    @Override
    public List<OrderResponse> getUserPage(int page, int pageSize) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        return getPageByUser(user, page, pageSize);
    }

    @Override
    public OrderResponse getUserOrder(long id) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Order order = orderRepository.findByIdAndUser(id, user)
                .orElseThrow(EntityNotFoundException::new);

        return objectMapper.convertValue(order, OrderResponse.class);
    }

    @Override
    public OrderResponse postUserOrder(OrderRequest request) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Order order = objectMapper.convertValue(request, Order.class);

        Set<Product> products = productRepository.findByIdIn(request.getProductIds());

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(EntityNotFoundException::new);

        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(EntityNotFoundException::new);

        Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
                .orElseThrow(EntityNotFoundException::new);

        Contact contact = contactRepository.findByIdAndUser(request.getContactId(), user)
                .orElseThrow(EntityNotFoundException::new);

        float price = (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        order.setUser(user);
        order.setProducts(products);
        order.setPayment(payment);
        order.setDelivery(delivery);
        order.setPrice(price);
        order.setContact(contact);
        order.setAddress(address);
        order.setDate(new Date());

        orderRepository.save(order);

        return objectMapper.convertValue(order, OrderResponse.class);
    }
}
