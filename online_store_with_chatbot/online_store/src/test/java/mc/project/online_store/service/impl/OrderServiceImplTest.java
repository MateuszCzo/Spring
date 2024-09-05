package mc.project.online_store.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import mc.project.online_store.dto.request.OrderRequest;
import mc.project.online_store.dto.response.OrderResponse;
import mc.project.online_store.model.*;
import mc.project.online_store.repository.*;
import mc.project.online_store.service.auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenGetPage_thenReturnsOrderResponseList() {
        int page = 0;
        int pageSize = 10;
        Order order = new Order();
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        OrderResponse response = new OrderResponse();

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        List<OrderResponse> serviceResponse = orderService.getPage(page, pageSize);

        verify(orderRepository).findAll(any(Pageable.class));

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenValidUserId_whenGetPageByUserId_thenOrderResponseList() {
        long userId = 1;
        int page = 0;
        int pageSize = 10;
        User user = new User();
        Order order = new Order();
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        OrderResponse response = new OrderResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user, pageRequest)).thenReturn(orderPage);
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        List<OrderResponse> serviceResponse = orderService.getPageByUserId(userId, page, pageSize);

        verify(userRepository).findById(userId);
        verify(orderRepository).findByUser(user, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUserId_whenGetPageByUserId_thenOrderResponseList() {
        long userId = 1;
        int page = 0;
        int pageSize = 10;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getPageByUserId(userId, page, pageSize));

        verify(userRepository).findById(userId);
    }

    @Test
    public void givenValidId_whenGetOrder_thenReturnsOrderResponse() {
        long id = 1;
        Order order = new Order();
        OrderResponse response = new OrderResponse();

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        OrderResponse serviceResponse = orderService.getOrder(id);

        verify(orderRepository).findById(id);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetOrder_thenOrderResponse() {
        long id = 1;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(id));

        verify(orderRepository).findById(id);
    }

    @Test
    public void givenValidIdAndOrderRequest_whenPutOrder_thenReturnsOrderResponse() throws JsonMappingException {
        long id = 1;
        OrderRequest request = new OrderRequest();
        request.setProductIds(new HashSet<>(List.of(2L)));
        Product product = new Product();
        product.setPrice(3);
        Set<Product> productSet = new HashSet<>(List.of(product));
        Order order = mock();
        OrderResponse response = new OrderResponse();

        when(productRepository.findByIdIn(request.getProductIds())).thenReturn(productSet);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(objectMapper.updateValue(order, request)).thenReturn(order);
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        OrderResponse serviceResponse = orderService.putOrder(id, request);

        verify(productRepository).findByIdIn(request.getProductIds());
        verify(orderRepository).findById(id);
        verify(objectMapper).updateValue(order, request);
        verify(order).setProducts(productSet);
        verify(order).setPrice(product.getPrice());

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidOrderRequest_whenPutOrder_thenThrowsEntityNotFoundException() {
        long id = 1;
        OrderRequest request = new OrderRequest();

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.putOrder(id, request));

        verify(orderRepository, never()).save(any());
    }

    @Test
    public void givenValidId_whenDeleteOrder_thenReturnsVoid() {
        long id = 1;
        Order order = new Order();

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        orderService.deleteOrder(id);

        verify(orderRepository).delete(order);
    }

    @Test
    public void givenInvalidId_whenDeleteOrder_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrder(id));

        verify(orderRepository, never()).delete(any());
    }

    @Test
    public void whenGetUserPage_thenReturnsOrderResponseList() {
        int page = 0;
        int pageSize = 10;
        User user = new User();
        Order order = new Order();
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        OrderResponse response = new OrderResponse();
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user, pageRequest)).thenReturn(orderPage);
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        List<OrderResponse> serviceResponse = orderService.getUserPage(page, pageSize);

        verify(userService).getLoggedInUser();
        verify(orderRepository).findByUser(user, pageRequest);

        assertNotNull(serviceResponse);
        assertEquals(1, serviceResponse.size());
        assertEquals(response, serviceResponse.get(0));
    }

    @Test
    public void givenInvalidUser_whenGetUserPage_thenReturnsOrderResponseList() {
        int page = 0;
        int pageSize = 10;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getUserPage(page, pageSize));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenValidId_whenGetUserOrder_thenReturnsOrderResponse() {
        long id = 1;
        User user = new User();
        Order order = new Order();
        OrderResponse response = new OrderResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(order));
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        OrderResponse serviceResponse = orderService.getUserOrder(id);

        verify(userService).getLoggedInUser();
        verify(orderRepository).findByIdAndUser(id ,user);

        assertEquals(response, serviceResponse);
    }

    @Test
    public void givenInvalidId_whenGetUserOrder_thenThrowsEntityNotFoundException() {
        long id = 1;
        User user = new User();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(orderRepository.findByIdAndUser(id, user)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getUserOrder(id));

        verify(userService).getLoggedInUser();
        verify(orderRepository).findByIdAndUser(id ,user);
    }

    @Test
    public void givenInvalidUser_whenGetUserOrder_thenThrowsEntityNotFoundException() {
        long id = 1;

        when(userService.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getUserOrder(id));

        verify(userService).getLoggedInUser();
    }

    @Test
    public void givenValidOrderRequest_whenPostUserOrder_thenReturnsOrderResponse() {
        OrderRequest request = new OrderRequest();
        request.setAddressId(1);
        request.setProductIds(new HashSet<>(List.of(2L)));
        request.setContactId(3);
        request.setDeliveryId(4);
        request.setPaymentId(5);
        User user = new User();
        Order order = mock();
        Product product = new Product();
        product.setPrice(6);
        Set<Product> productSet = new HashSet<>(List.of(product));
        Payment payment = new Payment();
        Delivery delivery = new Delivery();
        Address address = new Address();
        Contact contact = new Contact();
        OrderResponse response = new OrderResponse();

        when(userService.getLoggedInUser()).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(request, Contact.class)).thenReturn(contact);
        when(productRepository.findByIdIn(request.getProductIds())).thenReturn(productSet);
        when(paymentRepository.findById(request.getPaymentId())).thenReturn(Optional.of(payment));
        when(deliveryRepository.findById(request.getDeliveryId())).thenReturn(Optional.of(delivery));
        when(addressRepository.findByIdAndUser(request.getAddressId(), user)).thenReturn(Optional.of(address));
        when(contactRepository.findByIdAndUser(request.getContactId(), user)).thenReturn(Optional.of(contact));
        when(objectMapper.convertValue(order, OrderResponse.class)).thenReturn(response);

        OrderResponse serviceResponse = orderService.postUserOrder(request);

        verify(userService).getLoggedInUser();
        verify(objectMapper).convertValue(request, Contact.class);
        verify(productRepository).findByIdIn(request.getProductIds());
        verify(paymentRepository).findById(request.getPaymentId());
        verify(deliveryRepository).findById(request.getDeliveryId());
        verify(addressRepository).findByIdAndUser(request.getAddressId(), user);
        verify(contactRepository).findByIdAndUser(request.getContactId(), user);
        verify(order).setUser(user);
        verify(order).setProducts(productSet);
        verify(order).setPayment(payment);
        verify(order).setDelivery(delivery);
        verify(order).setPrice(product.getPrice());
        verify(order).setContact(contact);
        verify(order).setAddress(address);
        verify(orderRepository).save(order);

        assertEquals(response, serviceResponse);
    }
}