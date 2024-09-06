package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.exception.OrderException;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.admin.OrderService;
import mc.project.online_store.service.auth.UserService;
import mc.project.online_store.service.util.PriceCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService, mc.project.online_store.service.front.OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final ContactRepository contactRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final PriceCalculator priceCalculator;

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
    @Transactional
    public OrderResponse postUserOrder(OrderRequest request) {
        User user = userService.getLoggedInUser()
                .orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(EntityNotFoundException::new);

        Set<CartProduct> cartProducts = cartProductRepository.findByCart(cart);

        if (cartProducts.isEmpty()) {
            throw new OrderException("No products in cart");
        }

        Optional<CartProduct> invalidProduct = cartProducts.stream()
                .filter(cartProduct -> cartProduct.getProduct().getQuantity() < cartProduct.getQuantity() ||
                        !cartProduct.getProduct().isActive())
                .findFirst();

        if (invalidProduct.isPresent()) {
            throw new OrderException("Invalid product");
        }

        Order order = objectMapper.convertValue(request, Order.class);

        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(EntityNotFoundException::new);
        Delivery delivery = deliveryRepository.findById(request.getDeliveryId())
                .orElseThrow(EntityNotFoundException::new);
        Address address = addressRepository.findByIdAndUser(request.getAddressId(), user)
                .orElseThrow(EntityNotFoundException::new);
        Contact contact = contactRepository.findByIdAndUser(request.getContactId(), user)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderProduct> orderProducts = cartProducts.stream()
                .map(cartProduct -> objectMapper.convertValue(cartProduct, OrderProduct.class))
                .toList();
        orderProducts.forEach(orderProduct -> orderProduct.setOrder(order));

        Collection<ProductCalculationInformation> productInfoList = orderProducts.stream()
                .map(orderProduct -> (ProductCalculationInformation) orderProduct)
                .toList();
        float price = priceCalculator.calculate(productInfoList);

        order.setUser(user);
        order.setPayment(payment);
        order.setDelivery(delivery);
        order.setFinalPrice(price);
        order.setContact(contact);
        order.setAddress(address);
        order.setDate(new Date());

        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);
        cartProductRepository.deleteAll(cartProducts);
        cartRepository.delete(cart);

        return objectMapper.convertValue(order, OrderResponse.class);
    }
}
